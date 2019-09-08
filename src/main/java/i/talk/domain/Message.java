package i.talk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
public class Message {

	private String payload;
	@JsonSerialize(as = Participant.class)
    private Participant sender;
	@JsonProperty("timeStamp")
    private Long timeStamp;

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
