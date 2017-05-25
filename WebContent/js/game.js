function Game(params) {
    /**
     * Url of Websocket server
     */
    var url = params.serverUrl;
    if(url == undefined) {
        if(location.href.indexOf('index.html') >= 0) {
            url = location.href.replace('index.html', 'game');
        } else {
            url = location.href + 'game';
        }
        url = url.replace(/^https?\:\/\//, 'ws://');
    }

    /**
     * Data of game's client
     * @type {{socket: Connection, pname: null, status: (*|{}), connectBtn: (*|{}), disconnectBtn: (*|{})}}
     */
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

    /**
     * Set the given message as a status text
     * @param name Status message
     */
    var setStatus = function(name) {
        data.status.html(name);
        params.panel.find('#signinfo').html('');
    };

    setStatus('Niepołączony');

    /**
     * Set the given player name as a name of current player
     * @param pname Player name
     */
    var setPlayerName = function(pname) {
        data.pname = pname;
        var event = jQuery.Event("setname");
        event.name = pname;
        params.plist.trigger(event);
    };

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

    /**
     * Set the given player list
     * @param list List with all players
     */
    var setPlayerList = function(list) {
        var event = jQuery.Event("refresh");
        event.list = list;
        params.plist.trigger(event);
    };

    /**
     * Set the sign as a character of the current player
     * @param sign Character
     */
    var setSignInfo = function(sign) {
        var html = 'Twój znak:&nbsp;<text>'+sign+'</text>';
        params.panel.find('#signinfo').html(html);
    };

    /**
     * Activate a board
     * @param player Object with information about the current player
     */
    var activate = function(player) {
        var event = jQuery.Event("active");
        event.player = player;
        params.board.trigger(event);
        setStatus('Połączony, gra...');
        setSignInfo(player.sign);
    };

    /**
     * Deactivate a board
     * @param status A flag which informs if status message should be changed
     */
    var deactivate = function(status) {
        var event = jQuery.Event("inactive");
        params.board.trigger(event);
        if (status) {
            setStatus('Połączony, oczekuję na grę...');
        }
    };

    /**
     * Handler for an event of connecting to a game server
     * @param e Event object
     */
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

    /**
     * Handler for an event of closing connection
     * @param e Event object
     * @returns {boolean}
     */
    var closeConnection = function(e) {
        if(!data.socket.isConnected()) {
            gameAlert('Nie jesteś połączony.');
            return false;
        }
        data.socket.close();
    };

    /**
     * Action which contains operations of disconnecting the player
     * @param e Event object
     */
    var onDisconnect = function(e) {
        params.board.trigger('inactive');
        deactivate(false);
        setPlayerList([]);
        data.connectBtn.removeAttr('disabled');
        data.disconnectBtn.attr('disabled', '');
        setStatus('Niepołączony');
        if(e.wasClean) {
            gameAlert('Zostałeś rozłączony.');
        } else {
            gameAlert('Zostałeś rozłączony z komunikatem:'+ e.reason);
        }
    };

    /**
     * Set a handler for closing connection
     */
    data.socket.setCloseHandler(function(e){
        onDisconnect(e);
    });

    /**
     * Called when an enemy player made a move
     * @param move Move made by an enemy
     */
    var enemyMove = function(move) {
        var event = jQuery.Event("enemymove");
        event.x = move.x;
        event.y = move.y;
        params.board.trigger(event);
    };

    data.connectBtn.click(connect);
    data.disconnectBtn.click(closeConnection);

    /**
     * Add an handler for "mymove" event
     */
    params.board.on("mymove", function(e){
        data.socket.send('move', {
            "cell": {
                "x": e.x,
                "y": e.y
            }
        });
    });

    /**
     * Finish the board, highlight finishing cells and deactivate a board
     * @param positions Positions of finishing cells
     */
    var finishBoard = function(positions) {
        var event = jQuery.Event("finish");
        event.positions = positions;
        params.board.trigger(event);
        setTimeout(function(){deactivate(true);}, 3000);
    };

    /**
     * Turn back last move
     */
    var turnBackMove = function() {
        var event = jQuery.Event("turnback");
        params.board.trigger(event);
    };

    /**
     * Set a handler of all types of messages
     */
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
                deactivate(true);
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
                setTimeout(function(){deactivate(true);}, 3000);
                break;
            case "errormove":
                gameAlert('Ruch nie mógł zostać wykonany. Spróbuj ponownie.');
                turnBackMove();
                break;
        }
    });
}