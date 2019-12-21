package i.talk.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.NameChangeResponse;
import i.talk.domain.Participant;
import i.talk.domain.enums.OperationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import static i.talk.services.ResponseService.*;
import java.io.IOException;
import java.sql.Timestamp;

@Service
public class SendingService {

    private SimpMessagingTemplate template;

    SendingService(SimpMessagingTemplate template){
        this.template = template;
    }

    public void sendHasChangedNameResponse(String room, NameChangeResponse m) throws IOException {
        Message message = new Message(m.getOldName());
        message.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        m.setMessage(new ObjectMapper().writeValueAsString(message));
        String response = generateResponse(OperationEnum.CHANGE, m);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendJoinedPersonAsObject(String room, Participant person) {
        String response = generateResponse(OperationEnum.JOIN, person);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendHasJoinedMessageResponse(String room, String name) {
        Message message = generateMessage(name);
        String m = generateResponse(OperationEnum.JOIN, message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendHasLeftMessageResponse(String room, String name) {
        Message message = generateMessage(name);
        String m = generateResponse(OperationEnum.LEAVE, message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendDeleteResponse(String room, Message message){
        String m = generateResponse(OperationEnum.DELETE, message);
        this.template.convertAndSend("/chat/" + room, m);
    }
}
