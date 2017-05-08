package model;

public class Message {
	private String type;
	private Data data = new Data();
	
	public String getType() {
		return type;
	}
	public Message setType(String type) {
		this.type = type;
		return this;
	}
	public Data getData() {
		return data;
	}
	public Message setData(Data data) {
		this.data = data;
		return this;
	}
}
