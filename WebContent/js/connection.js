/**
 *
 * @param url Url where a WebSocket server is listening
 * @constructor
 */
function Connection(url) {
    /**
     * Socket object
     * @type {WebSocket|null}
     */
    var socket = null;

    /**
     * Message handler
     * @param type Message type
     * @param data Message date
     */
    var onMessage = function(type, data){};

    /**
     * Handler of opening connection
     * @param event Event object
     */
    var onOpen = function(event){};

    /**
     * Handler of closing connection
     * @param event Event object
     */
    var onClose = function(event){};

    /**
     * Open a connection and initialize socket's parameters
     */
    this.open = function() {
        socket = new WebSocket(url);

        socket.onmessage = function(event) {
            if (typeof event.data == 'string') {
                var message = JSON.parse(event.data);
                onMessage(message.type, message.data);
            } else {
                gameAlert('Odebrany komunikat z serwera nie może być poprawnie odczytany.');
            }
        };

        socket.onopen = onOpen;
        socket.onclose = onClose;

        socket.onerror = function(event) {
            gameAlert('Wystapił błąd podczas komunikacji z serwerem. Spróbuj oddświeżyć stronę i ponownie się połączyć.');
        };
    };
    /**
     *
     * @returns {boolean}
     */
    this.isConnected = function() {
        return socket != null && socket.readyState == WebSocket.OPEN;
    };

    /**
     * Close the opened socket
     */
    this.close = function() {
        socket.close();
        socket = null;
    };

    /**
     * Send a message with given parameters
     * @param type Message type
     * @param data Message data
     */
    this.send = function(type, data) {
        var message = {
            "type": type,
            "data": data
        };
        socket.send(JSON.stringify(message));
    };

    /**
     * Set handler which is responsible for receiving messages
     * @param handler Handler
     */
    this.setHandler = function(handler) {
        onMessage = handler;
    };

    /**
     * Set handler which is called during opening the connection
     * @param handler handler
     */
    this.setOpenHandler = function(handler) {
        onOpen = handler;
    };

    /**
     * Set handler which is called during closing the connection
     * @param handler Handler
     */
    this.setCloseHandler = function(handler) {
        onClose = handler;
    };
}