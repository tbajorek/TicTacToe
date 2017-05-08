function Connection(url) {
    var socket = null;
    var onMessage = function(type, data){};
    var onOpen = function(event){};
    var onClose = function(event){};

    this.open = function() {
        socket = new WebSocket(url);

        socket.onmessage = function(event) {
            var message = JSON.parse(event.data);
            onMessage(message.type, message.data);
        };

        socket.onopen = onOpen;
        socket.onclose = onClose;

        socket.onerror = function(event) {
            gameAlert('Wystapił błąd podczas komunikacji z serwerem. Spróbuj oddświeżyć stronę i ponownie się połączyć.');
        };
    };
    this.isConnected = function() {
        return socket != null;
    };
    this.close = function() {
        socket.close();
        socket = null;
    };
    this.send = function(type, data) {
        var message = {
            "type": type,
            "data": data
        };
        socket.send(JSON.stringify(message));
    };
    this.setHandler = function(handler) {
        onMessage = handler;
    };

    this.setOpenHandler = function(handler) {
        onOpen = handler;
    };

    this.setCloseHandler = function(handler) {
        onClose = handler;
    };
}