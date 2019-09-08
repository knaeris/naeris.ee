package i.talk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import i.talk.services.PubSubService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Data
//@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Participant{

	private String operation;

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

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subscribedMessages=" + subscribedMessages +
                '}';
    }

    private Set<Message> subscribedMessages = new HashSet<>();

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

	public ChatSession join(String session){
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
