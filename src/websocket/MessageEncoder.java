package websocket;

import model.Message;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class MessageEncoder implements Encoder.Text<Message> {
	@Override
	public String encode(Message message) throws EncodeException {
		Gson gson = new Gson();
		return gson.toJson(message);
	}
	
	@Override
	public void init(EndpointConfig ec) {
	
	}
	
	@Override
	public void destroy() {
	
	}
}
