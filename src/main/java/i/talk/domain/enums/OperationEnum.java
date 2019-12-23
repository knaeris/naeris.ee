package i.talk.domain.enums;

public enum OperationEnum {
    JOIN("JOIN"),
    SEND("SEND"),
    LEAVE("LEAVE"),
    DELETE("DELETE"),
    CHANGE("CHANGE"),
    VOTE("VOTE");

    public final String value;


     OperationEnum(String value) {
        this.value = value;
    }
}
