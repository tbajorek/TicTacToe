function Game(params) {
    var url = params.serverUrl;
    if(url == undefined) {
        if(location.href.indexOf('index.html') >= 0) {
            url = location.href.replace('index.html', 'game');
        } else {
            url = location.href + 'game';
        }
        url = url.replace(/^https?\:\/\//, 'ws://');
    }
    console.log(url);
    var data = {
        "socket": new Connection(url),
        "pname": null,
        "status": params.panel.find('#status'),
        "connectBtn": params.panel.find('#connect'),
        "disconnectBtn": params.panel.find('#disconnect')
    };

    params.board.boardgame();
    params.plist.playerlist();
    data.disconnectBtn.attr('disabled', '');

    var setPlayerName = function(pname) {
        data.pname = pname;
        var event = jQuery.Event("setname");
        event.name = pname;
        params.plist.trigger(event);
    };

    var setStatus = function(name) {
        data.status.html(name);
        params.panel.find('#signinfo').html('');
    };

    setStatus('Niepołączony');

    data.socket.setOpenHandler(function(e){
        setStatus('Połączony, oczekuję na grę...');
        data.disconnectBtn.removeAttr('disabled');
        data.connectBtn.attr('disabled', '');
        data.socket.send('newplayer', {
            "player": {
                "name": data.pname,
            }
        });
    });

    var setPlayerList = function(list) {
        var event = jQuery.Event("refresh");
        event.list = list;
        params.plist.trigger(event);
    };

    var setSignInfo = function(sign) {
        var html = 'Twój znak:&nbsp;<text>'+sign+'</text>';
        console.log(params.panel.find('#signinfo'));
        params.panel.find('#signinfo').html(html);
    };

    var activate = function(player) {
        var event = jQuery.Event("active");
        event.player = player;
        params.board.trigger(event);
        setStatus('Połączony, gra...');
        setSignInfo(player.sign);
    };

    var deactivate = function() {
        var event = jQuery.Event("inactive");
        params.board.trigger(event);
        setStatus('Połączony, oczekuję na grę...');
    };

    var connect = function(e) {
        vex.dialog.prompt({
            message: 'Podaj swoją nazwę użytkownika:',
            placeholder: 'Twoja nazwa',
            callback: function (value) {
                setPlayerName(value);
                data.socket.open();
            }
        });
    };

    var closeConnection = function(e) {
        if(!data.socket.isConnected()) {
            gameAlert('Nie jesteś połączony.');
            return false;
        }
        data.socket.close();
    };

    var onDisconnect = function(e) {
        params.board.trigger('inactive');
        deactivate();
        setPlayerList([]);
        data.connectBtn.removeAttr('disabled');
        data.disconnectBtn.attr('disabled', '');
        gameAlert('Zostałeś rozłączony.');
        setStatus('Niepołączony');
    };

    data.socket.setCloseHandler(function(e){
        onDisconnect(e);
    });

    var enemyMove = function(move) {
        var event = jQuery.Event("enemymove");
        event.x = move.x;
        event.y = move.y;
        params.board.trigger(event);
    };

    data.connectBtn.click(connect);
    data.disconnectBtn.click(closeConnection);

    params.board.on("mymove", function(e){
        data.socket.send('move', {
            "cell": {
                "x": e.x,
                "y": e.y
            }
        });
    });

    var finishBoard = function(positions) {
        var event = jQuery.Event("finish");
        event.positions = positions;
        params.board.trigger(event);
    };

    var turnBackMove = function() {
        var event = jQuery.Event("turnback");
        params.board.trigger(event);
    };

    data.socket.setHandler(function(type, sdata){
        switch(type) {
            case "list":
                setPlayerList(sdata.playersList.players);
                break;
            case "error":
                gameAlert(sdata.info);
                break;
            case "startgame":
                params.board.trigger('restart');
                activate(sdata.player);
                break;
            case "move":
                enemyMove(sdata.cell);
                break;
            case "walkover":
                gameAlert('Przeciwnik opuścił grę. Wygrałeś!');
                deactivate();
                break;
            case "win":
                gameAlert('Wygrałeś!');
                finishBoard(sdata.winners);
                break;
            case "lost":
                gameAlert('Przegrałeś!');
                finishBoard(sdata.winners);
                break;
            case "tie":
                gameAlert('Remis!');
                setTimeout(function(){deactivate();}, 3000);
                break;
            case "errormove":
                gameAlert('Ruch nie mógł zostać wykonany. Spróbuj ponownie.');
                turnBackMove();
                break;
        }
    });
}