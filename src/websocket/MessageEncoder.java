package websocket;

import model.Message;

import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

/**
 * Encoder of outgoing messages
 */
public class MessageEncoder implements Encoder.Text<Message> {
	/**
	 * Return an encoded string with JSON message
	 * @param message Message object which contains outgoing message
	 * @return
	 * @throws DecodeException Thrown when decoding can't be done
	 */
	@Override
	public String encode(Message message) throws EncodeException {
		Gson gson = new Gson();
		return gson.toJson(message);
	}
	
	/**
	 * Initialize the encoder
	 * @param ec EndpointConfig object
	 */
	@Override
	public void init(EndpointConfig ec) {
	
	}
	
	/**
	 * Destroy the encoder
	 */
	@Override
	public void destroy() {
	
	}
}
