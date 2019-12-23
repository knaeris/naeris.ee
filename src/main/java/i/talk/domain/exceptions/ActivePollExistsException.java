package i.talk.domain.exceptions;

public class ActivePollExistsException extends RuntimeException {

    public ActivePollExistsException() {
        super("Hääletus parasjagu toimub");
    }
}
