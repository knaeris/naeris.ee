package i.talk.domain;

import i.talk.services.PubSubService;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class Participant{

	private Long id = 0L;

	private String imageUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Participant(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Participant(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	//private Set<Message> subscriberMessages = new HashSet<>();

	//public void setSubscriberMessages(Set<Message> subscriberMessages) {
	//	this.subscriberMessages = subscriberMessages;
	//}

	//public Set<Message> getSubscriberMessages(){
	//	return subscriberMessages;
	//}

	//public void accept(Message message){
	//	subscriberMessages.add(message);
	//}



	//public void getParticipantMessages(Set<Message> subscriberMessages){
	//	this.subscriberMessages = subscriberMessages;
	//}

	/*public void send(Message message, PubSubService pubSubService) {
		pubSubService.addMessageToQueue(message);
		pubSubService.broadcast();
	}

	public void remove(Message message, PubSubService pubSubService){
		pubSubService.removeMessageFromAllParticipants(message);
	}*/

	public ChatSession join(String session, PubSubService pubSubService){
		pubSubService.addParticipant(session, this);
		return new ChatSession(session);
	}


	public void leave(String session,PubSubService pubSubService) {
		pubSubService.removeParticipant(session, this);
	}


	/*public Set<Message> getMessagesForParticipantOf(String session,PubSubService pubSubService) {
  		return pubSubService.getMessagesForParticipantOf(session, this);
	}*/

	public Participant changeNameTo(String newName, String session, PubSubService pubSubService) {
		setName(newName);
		Set<Participant> participants = pubSubService.getParticipantsOf(session);
		for(Participant p : participants){
			if(p.getId() == id){
				p.setName(newName);
			}
		}
		return this;
	}

}
