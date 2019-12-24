package i.talk.domain.unloaded;

import i.talk.domain.Message;
import i.talk.services.ChatService;

public class PublisherImpl {

    public void publish(Message message, ChatService chatService) {
        chatService.addMessageToQueue(message);
    }
}
