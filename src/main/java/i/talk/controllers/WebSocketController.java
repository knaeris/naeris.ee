package i.talk.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.services.MessageService;
import i.talk.services.ChatService;
import i.talk.services.SendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SendingService sendingService;
    @Autowired
    private ChatService chatService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("send/{room}")
    public void onReceivedMessage(@DestinationVariable String room, String message) throws IOException{
        this.template.convertAndSend("/chat/" + room, timeStamp(message));
    }

    @MessageMapping("join/{room}")
    public void joinChannel(@DestinationVariable String room, String joinedMessage) throws IOException {
        Participant person = generateParticipant(room, joinedMessage);
        addParticipantToRoom(room, person);
        sendJoinedPersonAsObject(room, person);
        Message message = messageService.generateMessage(joinedMessage);
        sendHasJoinedMessage(room, message);
    }

    @MessageMapping("leave/{room}")
    public void leaveChannel(@DestinationVariable String room, String message)throws IOException{
        removePerson(room, message);
        Message m = messageService.generateMessage(message);
        sendHasLeftMessage(room,m);
    }

    @MessageMapping("delete/{room}")
    public void deleteMessage(@DestinationVariable String room, String message) throws IOException{
        Message m = new ObjectMapper().readValue(message, Message.class);
        sendDeleteResponse(room, m);
    }

    private Message extractMessage(String message) throws IOException{
        return messageService.extractMessage(message);
    }

    private Participant generateParticipant(String room, String joinedMessage) throws IOException {
        String name = messageService.getPayload(joinedMessage);
        validateNameAlreadyDoesntExist(room, name);
        return chatService.generateParticipantFromName(name, room);
    }

    private void validateNameAlreadyDoesntExist(String room, String name) throws IOException{
       chatService.validateNameAlreadyDoesntExist(room, name);
    }

    private void removePerson(String room, String message) throws IOException {
        String name = messageService.getPayload(message);
        chatService.removeParticipant(room, name);
    }

    private void sendJoinedPersonAsObject(String room, Participant person) throws IOException {
        sendingService.sendJoinedPersonAsObject(room, person);
    }

	private void sendHasJoinedMessage(String room, Message message) throws IOException {
        sendingService.sendHasJoinedMessage(room, message);
	}

    private void sendHasLeftMessage(String room, Message message) throws IOException {
        sendingService.sendHasLeftMessage(room, message);
    }

    private void sendDeleteResponse(String room, Message message) throws IOException{
        sendingService.sendDeleteResponse(room,message);
    }

    private String timeStamp(String message) throws IOException {
        return messageService.timeStamp(message);
    }

    private void addParticipantToRoom(String room, Participant p){
	    chatService.addParticipant(room, p);
    }

    /*private void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
		sendingService.sendCurrentParticipantsOf(room);
	}*/

}
