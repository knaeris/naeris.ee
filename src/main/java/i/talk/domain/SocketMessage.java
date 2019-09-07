package i.talk.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketMessage extends Message {
    @JsonProperty("operation")
    private String operation;

    public SocketMessage(String payload, Participant sender, String operation) {
        super(payload, sender);
        this.operation = operation;
    }

    public SocketMessage(String operation) {
        this.operation = operation;
    }

    public SocketMessage(String payload, String operation) {
        super(payload);
        this.operation = operation;
    }
}
