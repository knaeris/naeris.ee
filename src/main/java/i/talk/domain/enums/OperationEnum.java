package i.talk.domain.enums;

public enum OperationEnum {
    JOIN("JOIN"),
    SEND("SEND"),
    LEAVE("LEAVE"),
    DELETE("DELETE");

    public final String value;


     OperationEnum(String value) {
        this.value = value;
    }
}
