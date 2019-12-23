package i.talk.domain;

import lombok.Data;

@Data
public class NameChangeResponse {

    private String oldName;
    private String newName;

    public NameChangeResponse(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }
}
