package uk.co.kasl.topicclient;

public class Message {
	public String name;
	public String message;
	
	public Message(String user, String messageText){
		name = user;
		message = messageText;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}
	
}