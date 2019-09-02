package i.talk.domain.interfaces;

import i.talk.domain.Message;
import i.talk.services.PubSubService;

public interface Publisher {

	void publish(Message message, PubSubService pubSubService);
}
