(function ( $ ) {
    /**
     * jQuery plugin which creates players list
     */
    $.fn.playerlist = function() {
        /**
         * List of all players who are logged
         * @type {Array}
         */
        var list = [];
        /**
         * Properties of current player
         * @type {{name: string|null, inGame: boolean}}
         */
        var player = {
            name: null,
            inGame: false
        };
        var element = $(this);

        var checkInGame = function() {
            for(var i in list) {
                if(list[i].name==player.name) {
                    player.inGame = list[i].inGame;
                }
            }
        };

        /**
         * Refresh the players list with data located in private property
         */
        var refresh = function() {console.log(list);
            var html = '';
            checkInGame();
            for(var i in list) {
                html += ('<div class="player'
                        +((i%2)?' row1':' row2')
                        +((list[i].name==player.name)?' current':'')
                        +((list[i].name!=player.name && list[i].inGame==true && player.inGame == true)?' enemy':'')
                        +((list[i].inGame==true)?' in-game':'')
                        +'">'
                        +((list[i].name==player.name)?'<span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;':'')
                        +list[i].name+'&nbsp;'+list[i].score+'</div>');
            }
            element.html(html);
        };

        /**
         * Handling of "refresh" event which refreshes the players list
         */
        element.on('refresh', function(e){
            list = e.list;
            refresh();
        });

        /**
         * Haandling of "setname" event which sets the given player name
         */
        element.on('setname', function(e){
            player.name = e.name;
        });
    };
}( jQuery ));