package i.talk.domain;

public class Message {

	//private String chat;
	private String payload;
	private Participant sender;

	public Message(String payload, Participant sender) {
		this.payload = payload;
		this.sender = sender;
	}

	/*public Message(String chat, String payload) {
		this.chat = chat;
		this.payload = payload;
	}*/

	public Message() {
	}

	public Message(String payload) {
		this.payload = payload;
	}

	/*public String getChat() {
		return chat;
	}

	public void setChat(String chat) {
		this.chat = chat;
	}*/

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
