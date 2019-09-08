package i.talk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.SocketMessage;
import i.talk.domain.enums.OperationEnum;
import i.talk.domain.enums.PictureUrlEnums;
import i.talk.services.PubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SimpMessagingTemplate template;
    @Autowired
    private PubSubService pubSubService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("send/message/{room}")
    public void onReceivedMessage(@DestinationVariable String room, String message) throws IOException{
        String operation = getOperationFrom(message);
        Message message1 = mapper.readValue(getPayload(message), Message.class);
        message1.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setOperation(operation);
        socketMessage.setPayload(mapper.writeValueAsString(message1));
        String result = mapper.writeValueAsString(socketMessage);
        this.template.convertAndSend("/chat/" + room, result);
    }

    String getPayload(String message) throws IOException{
        JsonNode jsonNode = mapper.readValue(message, JsonNode.class);
        JsonNode payloadNode = jsonNode.get("payload");
        return payloadNode.asText();
    }
    String getOperationFrom(String message) throws IOException{
        JsonNode jsonNode = mapper.readValue(message, JsonNode.class);
        JsonNode operationNode = jsonNode.get("operation");
        return operationNode.asText();
    }

    Participant generateParticipant(String name, String room){
        Long id = getFirstFreeIdIn(room);
        Participant participant = new Participant(id, name);
        String imageUrl = PictureUrlEnums.getRandom().label;
        participant.setImageUrl(imageUrl);
        return participant;
    }

    Participant addParticipantToRoom(String room, String name) throws IOException{
        if(pubSubService.getParticipantsOf(room).stream().map(x->x.getName()).collect(Collectors.toSet()).contains(name)){
            throw new IOException();
        }
        Participant participant = generateParticipant(name, room);
        pubSubService.addParticipant(room, participant);
        return participant;
    }

    String generateParticipantJoinedResponse(String room, String joinedMessage) throws IOException{
        String name = getPayload(joinedMessage);
        Participant participant = addParticipantToRoom(room, name);
        String operation = getOperationFrom(joinedMessage);
        String participantJson = mapper.writeValueAsString(participant);
        SocketMessage m = new SocketMessage(operation, participantJson);
        return mapper.writeValueAsString(m);
    }
    @MessageMapping("join/{room}")
    public void joinChannel(@DestinationVariable String room, String joinedMessage) throws IOException {
        sendParticipantInfo(room, joinedMessage);
		sendCurrentParticipantsOf(room);
        sendHasJoinedMessage(room, joinedMessage);
    }

    private void sendParticipantInfo(@DestinationVariable String room, String joinedMessage) throws IOException {
        String response = generateParticipantJoinedResponse(room, joinedMessage);
        this.template.convertAndSend("/chat/" + room, response);
    }

    private void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
		Set<Participant> participants = pubSubService.getParticipantsOf(room);
		String participantMessage = new ObjectMapper().writeValueAsString(participants);
		this.template.convertAndSend("/chat/" + room, participantMessage);
	}

	private void sendHasJoinedMessage(String room, String message1) throws IOException {
        String name = getPayload(message1);
		String message = composeAutomaticJoinedMessage(room, name);
		this.template.convertAndSend("/chat/" + room, message);
	}

	private String composeAutomaticJoinedMessage(String room, String name) throws IOException {
		Message message = new Message(name + " on liitunud ruumi : " + room);
		String m = mapper.writeValueAsString(message);
		SocketMessage me = new SocketMessage(OperationEnum.SEND.value, m);
		return new ObjectMapper().writeValueAsString(me);
	}

    private Long getFirstFreeIdIn(String chatName) {
        Set<Participant> participants = pubSubService.getParticipantsOf(chatName);
        Set<Long> ids = participants.stream().map(x -> x.getId()).collect(Collectors.toSet());
        Long id = 1L;
        while(ids.contains(id)){
            id++;
        }
        return id;
    }
}
