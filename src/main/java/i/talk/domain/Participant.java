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

    private Set<Message> subscribedMessages = new HashSet<>();

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
