package websocket;

import model.Message;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class MessageDecoder implements Decoder.Text<Message> {
	@Override
	public Message decode(String jsonMessage) throws DecodeException {
		Gson gson = new Gson();
		Message message = gson.fromJson(jsonMessage, Message.class);
		return message;
	}
	
	@Override
	public boolean willDecode(String jsonMessage) {
		try {
			return true;
		} catch (Exception e) {
			System.out.println("OKOKOK");
			return false;
		}
	}
	@Override
	public void init(EndpointConfig ec) {}

	@Override
	public void destroy() {}
}