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

    public String generateDeletedMessageResponse(Message message)throws IOException{
        return generateResponse(OperationEnum.DELETE, message);
    }

    public String generateHasJoinedMessageResponse(Message message) throws IOException {
        return generateResponse(OperationEnum.JOIN, message);
    }

    public String generateHasLeftMessageResponse(Message message) throws IOException {
        return generateResponse(OperationEnum.LEAVE, message);
    }

    public String generateJoinedParticipantResponse(Participant person) throws IOException{
        return generateResponse(OperationEnum.JOIN, person);
    }

    public String getPayload(String message) throws IOException{
        return getFieldFrom(message, "payload");
    }

    public String timeStamp(String message) throws IOException {
        Message message1 = extractMessage(message);
        message1.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        return generateResponse(OperationEnum.SEND, message1);
    }

    public Message generateMessage(String joinedMessage) throws IOException{
        String name = getPayload(joinedMessage);
        Message message = new Message(name);
        message.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        return message;
    }

    private String getFieldFrom(String message, String fieldName) throws IOException{
        JsonNode jsonNode = mapper.readValue(message, JsonNode.class);
        JsonNode operationNode = jsonNode.get(fieldName);
        return operationNode.asText();
    }

    private String generateResponse(OperationEnum operation, Object o) throws IOException{
        String participantJson = mapper.writeValueAsString(o);
        SocketMessage m = new SocketMessage(operation.value, participantJson);
        return mapper.writeValueAsString(m);
    }

    public Message extractMessage(String message) throws IOException{
        String payload = getPayload(message);
        return mapper.readValue(payload, Message.class);
    }
}
