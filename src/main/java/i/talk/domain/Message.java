package i.talk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Setter;

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

    public Participant getSender() {
        return sender;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }
}
