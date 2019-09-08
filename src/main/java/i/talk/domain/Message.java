package i.talk.domain;

import lombok.Setter;

@Setter
public class Message {

	private String payload;
    private Participant sender;

	public Message(String payload, Participant sender) {
		this.payload = payload;
		this.sender = sender;
	}

	public Message() {
	}

	public Message(String payload) {
		this.payload = payload;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
