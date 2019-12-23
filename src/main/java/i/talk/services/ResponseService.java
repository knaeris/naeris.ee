package i.talk.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.SocketMessage;
import i.talk.domain.enums.OperationEnum;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class ResponseService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String timeStamp(String message) {
        try {
            Message message1 = mapper.readValue(message, Message.class);
            message1.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
            return generateResponse(OperationEnum.SEND, message1);
        } catch (IOException e) {

        }
        return null;
    }

    public static String generateResponse(OperationEnum operation, Object o) {
        try {
            String participantJson = mapper.writeValueAsString(o);
            SocketMessage m = new SocketMessage(operation.value, participantJson);
            return mapper.writeValueAsString(m);
        } catch (IOException e) {

        }
        return null;
    }

}
