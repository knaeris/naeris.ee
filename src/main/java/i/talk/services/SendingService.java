package i.talk.services;

import i.talk.domain.Message;
import i.talk.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendingService {

    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    SendingService(SimpMessagingTemplate template){
        this.template = template;
    }

    public void sendJoinedPersonAsObject(String room, Participant person) throws IOException {
        String response = messageService.generateJoinedParticipantResponse(person);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendHasJoinedMessage(String room, Message message) throws IOException {
        String m = messageService.generateHasJoinedMessageResponse(message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendHasLeftMessage(String room, Message message) throws IOException {
        String m = messageService.generateHasLeftMessageResponse(message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendDeleteResponse(String room, Message message) throws IOException{
        String m = messageService.generateDeletedMessageResponse(message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    /*public void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
        Set<Participant> participants = chatService.getParticipantsOf(room);
        String participantMessage = new ObjectMapper().writeValueAsString(participants);
        this.template.convertAndSend("/chat/" + room, participantMessage);
    }*/
}
