package i.talk.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.NameChangeResponse;
import i.talk.domain.Participant;
import i.talk.domain.enums.OperationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class SendingService {

    private SimpMessagingTemplate template;

    @Autowired
    private ResponseService messageService;

    SendingService(SimpMessagingTemplate template){
        this.template = template;
    }

    public void sendHasChangedNameResponse(String room, NameChangeResponse m) throws IOException {
        Message message = new Message(m.getOldName());
        message.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        m.setMessage(new ObjectMapper().writeValueAsString(message));
        String response = messageService.generateResponse(OperationEnum.CHANGE, m);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendJoinedPersonAsObject(String room, Participant person) throws IOException {
        String response = messageService.generateResponse(OperationEnum.JOIN, person);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendHasJoinedMessageResponse(String room, String name) throws IOException {
        Message message = messageService.generateMessage(name);
        String m = messageService.generateResponse(OperationEnum.JOIN, message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendHasLeftMessageResponse(String room, String name) throws IOException {
        Message message = messageService.generateMessage(name);
        String m = messageService.generateResponse(OperationEnum.LEAVE, message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendDeleteResponse(String room, Message message) throws IOException{
        String m = messageService.generateResponse(OperationEnum.DELETE, message);
        this.template.convertAndSend("/chat/" + room, m);
    }
    /*public void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
        Set<Participant> participants = chatService.getParticipantsOf(room);
        String participantMessage = new ObjectMapper().writeValueAsString(participants);
        this.template.convertAndSend("/chat/" + room, participantMessage);
    }*/
}
