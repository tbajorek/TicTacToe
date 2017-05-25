package model;

/**
 * Class which is used as a template of messages between server and clients
 */
public class Message {
	/**
	 * A type of the message
	 */
	private String type;
	
	/**
	 * Object with information sent in the message
	 */
	private Data data = new Data();
	
	/**
	 * Return a type of the message
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set a type of the message
	 * @param type Type of the message
	 * @return
	 */
	public Message setType(String type) {
		this.type = type;
		return this;
	}
	
	/**
	 * Return an object with information sent in the message
	 * @return
	 */
	public Data getData() {
		return data;
	}
	
	/**
	 * Set an object with information sent in the message
	 * @param data Object with information
	 * @return
	 */
	public Message setData(Data data) {
		this.data = data;
		return this;
	}
}
