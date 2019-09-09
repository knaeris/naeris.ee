package i.talk.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.SocketMessage;
import i.talk.domain.enums.OperationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class MessageService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ChatService chatService;

    public String composeAutomaticJoinedMessage(String room, String joinedMessage) throws IOException {
        String name = getPayload(joinedMessage);
        Message message = new Message(name + " on liitunud ruumi : " + room);
        message.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        String m = mapper.writeValueAsString(message);
        SocketMessage me = new SocketMessage(OperationEnum.SEND.value, m);
        return new ObjectMapper().writeValueAsString(me);
    }

    public String generateParticipantJoinedResponse(String room, String joinedMessage) throws IOException{
        String name = getPayload(joinedMessage);
        Participant participant = chatService.addParticipantToRoom(room, name);
        String operation = getOperationFrom(joinedMessage);
        String participantJson = mapper.writeValueAsString(participant);
        SocketMessage m = new SocketMessage(operation, participantJson);
        return mapper.writeValueAsString(m);
    }

    public String timeStamp(String message) throws IOException {
        SocketMessage socketMessage = addTimeStampTo(message);
        return mapper.writeValueAsString(socketMessage);
    }

    private SocketMessage addTimeStampTo(String message) throws IOException {
        String operation = getOperationFrom(message);
        Message message1 = mapper.readValue(getPayload(message), Message.class);
        message1.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        SocketMessage socketMessage = new SocketMessage();
        socketMessage.setOperation(operation);
        socketMessage.setPayload(mapper.writeValueAsString(message1));
        return socketMessage;
    }

    public String getPayload(String message) throws IOException{
        JsonNode jsonNode = mapper.readValue(message, JsonNode.class);
        JsonNode payloadNode = jsonNode.get("payload");
        return payloadNode.asText();
    }
    public String getOperationFrom(String message) throws IOException{
        JsonNode jsonNode = mapper.readValue(message, JsonNode.class);
        JsonNode operationNode = jsonNode.get("operation");
        return operationNode.asText();
    }

    public String composeSocketMessage(String message) throws IOException{
        String name = getPayload(message);
        String operation = getOperationFrom(message);
        SocketMessage m = new SocketMessage();
        Message me= new Message(name);
        me.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        String meString = mapper.writeValueAsString(me);
        m.setOperation(operation);
        m.setPayload(meString);
        return mapper.writeValueAsString(m);
    }
}
