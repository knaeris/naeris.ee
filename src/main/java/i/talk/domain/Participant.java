package i.talk.domain;

import i.talk.services.ChatService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Data
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

}
