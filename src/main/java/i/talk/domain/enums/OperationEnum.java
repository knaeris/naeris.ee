package i.talk.domain.enums;

public enum OperationEnum {
    JOIN("JOIN"),
    SEND("SEND");

    public final String value;


    private OperationEnum(String value) {
        this.value = value;
    }
}
