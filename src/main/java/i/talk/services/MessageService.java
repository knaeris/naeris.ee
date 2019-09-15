package i.talk.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.SocketMessage;
import i.talk.domain.enums.OperationEnum;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class MessageService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String timeStamp(String message) throws IOException {
        Message message1 = mapper.readValue(message, Message.class);
        message1.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        return generateResponse(OperationEnum.SEND, message1);
    }

    public Message generateMessage(String name) {
        Message message = new Message(name);
        message.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        return message;
    }

    public String generateResponse(OperationEnum operation, Object o) throws IOException {
        String participantJson = mapper.writeValueAsString(o);
        SocketMessage m = new SocketMessage(operation.value, participantJson);
        return mapper.writeValueAsString(m);
    }
}
