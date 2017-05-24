(function ( $ ) {

    $.fn.boardgame = function() {
        var model = {
            board: null,
            my: null,
            enemy: null,
            cells: null,
            canClick: false,
            active: false,
            lastMove: {
                x: null,
                y: null
            }
        };
        var element = $(this);
        if (!model.active) {
            element.addClass('inactive');
        }

        var isCorrectPosition = function(x, y) {
            return parseInt(x) < model.board.length && parseInt(y) < model.board[0].length;
        };

        var getValue = function(x, y) {
            if (isCorrectPosition(x, y)) {
                return model.board[parseInt(y)][parseInt(x)];
            } else {
                throw "Zla pozycja";
            }
        };

        var isFree = function(x, y) {
            return getValue(x, y) == '';
        };

        var refresh = function() {
            for(var y in model.cells) {
                for(var x in model.cells[y]) {
                    model.cells[y][x].html(getValue(x, y));
                    if (getValue(x, y) != '') {
                        model.cells[y][x].addClass('busy');
                    } else {
                        model.cells[y][x].removeClass('busy');
                    }
                }
            }
        };

        var setMy = function(x,y) {
            if (isCorrectPosition(x, y)) {
                model.board[parseInt(y)][parseInt(x)] = model.my;
                model.lastMove.x = x;
                model.lastMove.y = y;
                refresh();
                return true;
            } else {
                return false;
            }
        };

        var setEnemy = function(x,y) {
            if (isCorrectPosition(x, y)) {
                model.board[parseInt(y)][parseInt(x)] = model.enemy;
                refresh();
                return true;
            } else {
                return false;
            }
        };

        var prepareElementsStruct = function(el) {
            var elStruct = [];
            el.find('tr').each(function(k, o){
                var y = elStruct.length;
                elStruct[y] = [];
                $(o).find('td').each(function(k, o){
                    var x = elStruct[y].length;
                    elStruct[y][x] = $(o);
                    elStruct[y][x].data('x', x).data('y', y);
                });
            });
            return elStruct;
        };

        var setWinner = function(positions) {
            for (var i in positions) {
                var x = parseInt(positions[i].x);
                var y = parseInt(positions[i].y);
                if (getValue(x, y) == model.my) {
                    model.cells[y][x].addClass('win');
                } else {
                    model.cells[y][x].addClass('lost');
                }
            }
        };

        var switch2game = function() {
            model.canClick = true;
            element.addClass('gaming');
            element.removeClass('waiting');
        };

        var switch2wait = function() {
            model.canClick = false;
            element.addClass('waiting');
            element.removeClass('gaming');
        };

        var initSigns = function(my) {
            if (my == 'X') {
                model.my = 'X';
                model.enemy = 'O';
            } else {
                model.my = 'O';
                model.enemy = 'X';
            }
            if (model.my == 'O') {
                switch2game();
            } else {
                switch2wait();
            }
        };

        var init = function(my) {
            model.board = [
                ['','',''],
                ['','',''],
                ['','','']
            ];
            if (my != undefined) {
                initSigns(my);
            }
            model.cells = prepareElementsStruct(element);
            refresh();
        };

        var restart = function(my) {
            model.active = false;
            element.removeClass('waiting');
            element.removeClass('gaming');
            element.find('.win').removeClass('win');
            element.find('.lost').removeClass('lost');
            init(my);
        };

        var deactivate = function() {
            if (model.active == true) {
                model.active = false;
                element.addClass('inactive');
            }
            restart();
        };

        init();
        element.filter('.inactive').click(function(e){
            if (!model.active) {
                gameAlert('Wciąż oczekujesz na rozpoczęcie gry.');
                e.stopPropagation();
                e.cancelBubble = true;
                return false;
            }
        });
        element.find('td').click(function(e){
            if (model.active) {
                var x = parseInt($(e.target).data('x'));
                var y = parseInt($(e.target).data('y'));
                if (isFree(x, y)) {
                    if(model.canClick) {
                        setMy(x, y);
                        switch2wait();
                        var event = jQuery.Event("mymove");
                        event.x = x;
                        event.y = y;
                        element.trigger(event);
                    } else {
                        gameAlert('Nie możesz tego teraz zrobić. Poczekaj na swoją kolejkę.');
                    }
                } else {
                    gameAlert('Nie możesz ustawić zajętego pola.');
                }
            }
        });
        element.on('restart', function(e) {
            restart(e.sign);
        });
        element.on('turnback', function(e) {
            model.board[parseInt(model.lastMove.y)][parseInt(model.lastMove.x)] = '';
        });
        element.on('active', function(e) {
            if (model.active == false) {
                model.active = true;
                initSigns(e.player.sign);
                if(model.canClick == true) {
                    gameAlert('Zaczynasz grę.');
                }
                element.removeClass('inactive');
            }
        });
        element.on('inactive', function(e) {
            deactivate();
        });
        element.on('enemymove', function(e) {
            if (model.canClick == false) {
                setEnemy(e.x, e.y);
                switch2game();
            }
        });

        element.on('finish', function(e) {
            setWinner((e.positions));
        });
    };

}( jQuery ));