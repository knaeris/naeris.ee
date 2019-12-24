package i.talk.domain;

import lombok.Getter;

@Getter
public class Vote {

    private boolean value;

    private Long voterId;

    public Vote(boolean value, Long voterId) {
        this.value = value;
        this.voterId = voterId;
    }

    public Vote() {
    }
}
