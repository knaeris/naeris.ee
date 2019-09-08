package i.talk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.SocketMessage;
import i.talk.domain.enums.PictureUrlEnums;
import i.talk.services.PubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {


    private final SimpMessagingTemplate template;
    @Autowired
    private PubSubService pubSubService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("send/message/{room}")
    public void onReceivedMessage(@DestinationVariable String room, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(message, JsonNode.class);
        JsonNode oayloadNode = jsonNode.get("payload");
        String payload = oayloadNode.asText();
        JsonNode sender = jsonNode.get("sender");
        JsonNode idNode = sender.get("id");
        Long id = Long.parseLong(idNode.asText());
        JsonNode nameNode = sender.get("name");
        String name = nameNode.asText();
        Message messageObj = new Message(payload);
        Participant participant = new Participant(id, name);
        messageObj.setSender(participant);
        this.template.convertAndSend("/chat/" + room, message);
    }

    @MessageMapping("join/{room}")
    public void joinChannel(@DestinationVariable String room, String joinedMessage) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Long id = getFirstFreeIdIn(room);
        JsonNode jsonNode = mapper.readValue(joinedMessage, JsonNode.class);
        JsonNode nameNode = jsonNode.get("name");
        String name = nameNode.asText();
        JsonNode operationNode = jsonNode.get("operation");
        String operation = operationNode.asText();
        Participant participant = new Participant(id, name);
        String imageUrl = PictureUrlEnums.getRandom().label;
        participant.setImageUrl(imageUrl);
        participant.setOperation(operation);
		String result = mapper.writeValueAsString(participant);
        if(pubSubService.getParticipantsOf(room).stream().map(x->x.getName()).collect(Collectors.toSet()).contains(name)){
            throw new IOException();
        }
        pubSubService.addParticipant(room, participant);
		this.template.convertAndSend("/chat/" + room, result);
		sendHasJoinedMessage(room, name);
		sendCurrentParticipantsOf(room);
    }

	private void sendCurrentParticipantsOf(String room) throws JsonProcessingException {
		Set<Participant> participants = pubSubService.getParticipantsOf(room);
		String participantMessage = new ObjectMapper().writeValueAsString(participants);
		this.template.convertAndSend("/chat/" + room, participantMessage);
	}

	private void sendHasJoinedMessage(String room, String name) throws IOException {
		String message = getMessage(room, name);
		this.template.convertAndSend("/chat/" + room, message);
	}

	private String getMessage(String room, String name) throws IOException {
		Message message = new SocketMessage("SEND");
		message.setPayload(name + " on liitunud ruumi : " + room);
		return new ObjectMapper().writeValueAsString(message);
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
