package websocket;

import model.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

/**
 * Decoder of incoming messages
 */
public class MessageDecoder implements Decoder.Text<Message> {
	/**
	 * Return a decoded Message object
	 * @param jsonMessage String with JSON which contains incoming message
	 * @return
	 * @throws DecodeException Thrown when decoding can't be done
	 */
	@Override
	public Message decode(String jsonMessage) throws DecodeException {
		Gson gson = new Gson();
		Message message = gson.fromJson(jsonMessage, Message.class);
		return message;
	}
	
	/**
	 * Function can disable encoding if it returns false
	 * @param jsonMessage String with JSON which contains incoming message
	 * @return
	 */
	@Override
	public boolean willDecode(String jsonMessage) {
		return true;
	}
	
	/**
	 * Initialize the decoder
	 * @param ec EndpointConfig object
	 */
	@Override
	public void init(EndpointConfig ec) {}

	/**
	 * Destroy the decoder
	 */
	@Override
	public void destroy() {}
}