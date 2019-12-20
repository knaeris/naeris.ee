package i.talk.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.NameChangeResponse;
import i.talk.domain.Participant;
import i.talk.services.ChatService;
import i.talk.services.ResponseService;
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
    private ResponseService responseService;
    @Autowired
    private SendingService sendingService;
    @Autowired
    private ChatService chatService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("send/{room}")
    public void onReceivedMessage(@DestinationVariable String room, String message) throws IOException {
        this.template.convertAndSend("/chat/" + room, timeStamp(message));
    }

    @MessageMapping("join/{room}")
    public void joinChannel(@DestinationVariable String room, String name) throws IOException {
        if (!isNameAlreadyTaken(room, name)) {
            Participant person = generateParticipant(room, name);
            addParticipantToRoom(room, person);
            sendJoinedPersonAsObject(room, person);
            sendHasJoinedMessageResponse(room, name);
        }
    }

    @MessageMapping("leave/{room}")
    public void leaveChannel(@DestinationVariable String room, String name) throws IOException {
        removePerson(room, name);
        sendHasLeftMessageResponse(room, name);
    }

    @MessageMapping("delete/{room}")
    public void deleteMessage(@DestinationVariable String room, String message) throws IOException {
        Message m = new ObjectMapper().readValue(message, Message.class);
        sendDeleteResponse(room, m);
    }

    @MessageMapping("changeName/{room}/{name}")
    public void changeName(@DestinationVariable String room, @DestinationVariable String name, Long id) throws IOException{
        Participant p = this.chatService.getParticipantInChatById(room, id);
        String oldName = p.getName();
        this.chatService.removeParticipant(room, p.getName());
        p.setName(name);
        this.chatService.addParticipant(room, p);
        sendNameChangedResponse(room, new NameChangeResponse(oldName, name));
    }

    private void sendNameChangedResponse(String room, NameChangeResponse m) throws IOException{
        this.sendingService.sendHasChangedNameResponse(room, m);
    }

    private Participant generateParticipant(String room, String name) {
        Long id = chatService.getFirstFreeIdIn(room);
        return chatService.generateParticipant(name, id);
    }

    private boolean isNameAlreadyTaken(String room, String name) {
        return chatService.isNameAlreadyTaken(room, name);
    }

    private void removePerson(String room, String name) {
        chatService.removeParticipant(room, name);
    }

    private void sendJoinedPersonAsObject(String room, Participant person) throws IOException {
        sendingService.sendJoinedPersonAsObject(room, person);
    }

    private void sendHasJoinedMessageResponse(String room, String name) throws IOException {
        sendingService.sendHasJoinedMessageResponse(room, name);
    }

    private void sendHasLeftMessageResponse(String room, String name) throws IOException {
        sendingService.sendHasLeftMessageResponse(room, name);
    }

    private void sendDeleteResponse(String room, Message message) throws IOException {
        sendingService.sendDeleteResponse(room, message);
    }

    private String timeStamp(String message) throws IOException {
        return responseService.timeStamp(message);
    }

    private void addParticipantToRoom(String room, Participant p) {
        chatService.addParticipant(room, p);
    }

    /*private void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
		sendingService.sendCurrentParticipantsOf(room);
	}*/

}
