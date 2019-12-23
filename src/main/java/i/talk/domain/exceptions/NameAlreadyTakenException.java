package i.talk.domain.exceptions;

public class NameAlreadyTakenException extends RuntimeException {

    public NameAlreadyTakenException() {
        super("Nimi on juba selles ruumis kasutusel");
    }
}
