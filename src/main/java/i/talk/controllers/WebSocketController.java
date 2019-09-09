package i.talk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.SocketMessage;
import i.talk.domain.enums.OperationEnum;
import i.talk.domain.enums.PictureUrlEnums;
import i.talk.services.MessageService;
import i.talk.services.ChatService;
import i.talk.services.SendingService;
import i.talk.services.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ValidateService validateService;
    @Autowired
    private SendingService sendingService;
    @Autowired
    private ChatService chatService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("send/message/{room}")
    public void onReceivedMessage(@DestinationVariable String room, String message) throws IOException{
        this.template.convertAndSend("/chat/" + room, timeStamp(message));
    }

    @MessageMapping("join/{room}")
    public void joinChannel(@DestinationVariable String room, String joinedMessage) throws IOException {
        validateJoinRequest(room, joinedMessage);
        sendParticipantInfo(room, joinedMessage);
		sendCurrentParticipantsOf(room);
        sendHasJoinedMessage(room, joinedMessage);
    }

    @MessageMapping("leave/{room}")
    public void leaveChannel(@DestinationVariable String room, String message)throws IOException{
        String name = messageService.getPayload(message);
        String result = messageService.composeSocketMessage(message);
        chatService.removeParticipant(room, name);
        this.template.convertAndSend("/chat/" + room, result);
    }

    private void validateJoinRequest(String room, String joinedMessage) throws IOException{
        validateService.validateJoinRequest(room,joinedMessage);
    }

    private void sendParticipantInfo(String room, String joinedMessage) throws IOException {
        sendingService.sendParticipantInfo(room, joinedMessage);
    }

    private void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
		sendingService.sendCurrentParticipantsOf(room);
	}

	private void sendHasJoinedMessage(String room, String joinedMessage) throws IOException {
        sendingService.sendHasJoinedMessage(room, joinedMessage);
	}

    private String timeStamp(String message) throws IOException {
        return messageService.timeStamp(message);
    }
}
