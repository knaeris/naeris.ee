package i.talk.domain.unloaded;

import i.talk.domain.Message;
import i.talk.services.ChatService;

import java.util.HashSet;
import java.util.Set;

public abstract class Subscriber {

    private Set<Message> subscriberMessages = new HashSet<Message>();

    public Set<Message> getSubscriberMessages() {
        return subscriberMessages;
    }

    public void setSubscriberMessages(Set<Message> subscriberMessages) {
        this.subscriberMessages = subscriberMessages;
    }

    public abstract void addSubscriber(String topic, ChatService chatService);

    public abstract void unSubscribe(String topic, ChatService chatService);

    public abstract void getMessagesForSubscriberOf(String topic, ChatService chatService);

	/*public void printMessages(){
		for(Message message : subscriberMessages){
			System.out.println("Message Topic -> "+ message.getChat() + " : " + message.getPayload());
		}
	}*/
}
