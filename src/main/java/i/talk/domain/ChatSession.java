package i.talk.domain;

import java.util.HashSet;
import java.util.Set;

public class ChatSession {

	private String name;
	private Set<Participant> participants = new HashSet<>();

	public ChatSession(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<Participant> participants) {
		this.participants = participants;
	}

}
