package i.talk.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class SendingService {

    private SimpMessagingTemplate template;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;

    SendingService(SimpMessagingTemplate template){
        this.template = template;
    }

    public void sendParticipantInfo(String room, String joinedMessage) throws IOException {
        String response = messageService.generateParticipantJoinedResponse(room, joinedMessage);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
        Set<Participant> participants = chatService.getParticipantsOf(room);
        String participantMessage = new ObjectMapper().writeValueAsString(participants);
        this.template.convertAndSend("/chat/" + room, participantMessage);
    }

    public void sendHasJoinedMessage(String room, String joinedMessage) throws IOException {
        String message = composeAutomaticJoinedMessage(room, joinedMessage);
        this.template.convertAndSend("/chat/" + room, message);
    }

    private String composeAutomaticJoinedMessage(String room, String joinedMessage) throws IOException {
        return messageService.composeAutomaticJoinedMessage(room,joinedMessage);
    }
}
