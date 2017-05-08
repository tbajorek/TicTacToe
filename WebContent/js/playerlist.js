(function ( $ ) {

    $.fn.playerlist = function() {
        var list = [];
        var player = {
            name: null,
            inGame: false
        };
        var element = $(this);

        var refresh = function() {
            var html = '';
            for(var i in list) {
                if(list[i].name==player.name) {
                    player.inGame = list[i].inGame;
                }
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

        element.on('refresh', function(e){
            list = e.list;
            refresh();
        });

        element.on('setname', function(e){
            player.name = e.name;
        });

        refresh();
    };
}( jQuery ));