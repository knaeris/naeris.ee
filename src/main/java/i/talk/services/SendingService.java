package i.talk.services;

import i.talk.domain.KickVotePoll;
import i.talk.domain.Message;
import i.talk.domain.NameChangeResponse;
import i.talk.domain.Participant;
import i.talk.domain.enums.OperationEnum;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static i.talk.services.ResponseService.generateResponse;

@Service
public class SendingService {

    private SimpMessagingTemplate template;

    SendingService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendHasChangedNameResponse(String room, NameChangeResponse m) {
        String response = generateResponse(OperationEnum.CHANGE, m);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendJoinedPersonAsObject(String room, Participant person) {
        String response = generateResponse(OperationEnum.JOIN, person);
        this.template.convertAndSend("/chat/" + room, response);
    }

    public void sendHasJoinedSystemMessage(String room, String name) {
        String text = name + " on liitunud ruumiga";
        sendSystemMessage(room, text);
    }

    public void sendHasLeftMessageSystemMessage(String room, String name) {
        String text = name + " on lahkunud ruumist";
        sendSystemMessage(room, text);
    }

    public void sendHasKickedFromRoomSystemMessage(String room, String name, String reason) {
        String text = name + " kickiti ruumist kuna " + reason;
        sendSystemMessage(room, text);
    }

    public void sendSomeOneHasLeftOrKickedResponse(String room, Participant participant) {
        String m = generateResponse(OperationEnum.LEAVE, participant);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendDeleteResponse(String room, Message message) {
        String m = generateResponse(OperationEnum.DELETE, message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendVoteResponse(String room, KickVotePoll vote) {
        String v = generateResponse(OperationEnum.VOTE, vote);
        this.template.convertAndSend("/chat/" + room, v);
    }

    public void sendHasChangedNameSystemMessage(String room, NameChangeResponse m) {
        String text = m.getOldName() + " muutis enda nimeks: " + m.getNewName();
        sendSystemMessage(room, text);
    }

    public void sendHasCalledAVoteSystemMessage(KickVotePoll poll) {
        String text = poll.getVoteCaller().getName() + " algatas hääletuse kickimaks: " + poll.getPersonToKick().getName();
        sendSystemMessage(poll.getChatName(), text);
    }

    private void sendSystemMessage(String room, String messageText) {
        Message message = new Message(messageText, null);
        message.setTimeStamp(new Timestamp(System.currentTimeMillis()).getTime());
        String m = generateResponse(OperationEnum.SEND, message);
        this.template.convertAndSend("/chat/" + room, m);
    }

    public void sendVoteFailedSystemMessage(String chatName) {
        String text = "Hääletus ei läinud läbi";
        sendSystemMessage(chatName, text);
    }
}
