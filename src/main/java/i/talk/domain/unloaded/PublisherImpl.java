package i.talk.domain.unloaded;

import i.talk.domain.Message;
import i.talk.services.PubSubService;

public class PublisherImpl {

	public void publish(Message message, PubSubService pubSubService) {
		pubSubService.addMessageToQueue(message);
	}
}
