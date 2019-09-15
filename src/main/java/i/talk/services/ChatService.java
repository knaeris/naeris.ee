package i.talk.services;

import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.enums.PictureUrlEnums;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

	private Map<String, Set<Participant>> chatParticipantsMap = new HashMap<String, Set<Participant>>();

	private Queue<Message> messageQueue = new LinkedList<Message>();

	public void addMessageToQueue(Message message) {
		messageQueue.add(message);
	}

	public boolean isNameAlreadyTaken(String room, String name){
		if(getParticipantsOf(room).stream().map(x->x.getName()).collect(Collectors.toSet()).contains(name)){
			return true;
		}
		return false;
	}
	public Participant generateParticipant(String name, Long id){
		Participant participant = new Participant(id, name);
		String imageUrl = PictureUrlEnums.getRandom().label;
		participant.setImageUrl(imageUrl);
		return participant;
	}

	public Long getFirstFreeIdIn(String chatName) {
		Set<Participant> participants = getParticipantsOf(chatName);
		Set<Long> ids = participants.stream().map(x -> x.getId()).collect(Collectors.toSet());
		Long id = 1L;
		while(ids.contains(id)){
			id++;
		}
		return id;
	}

	public void addParticipant(String session, Participant participant) {
		if (chatParticipantsMap.containsKey(session)) {
			Set<Participant> subscribers = chatParticipantsMap.get(session);
			subscribers.add(participant);
			chatParticipantsMap.put(session, subscribers);
		} else {
			Set<Participant> subscribers = new HashSet<Participant>();
			subscribers.add(participant);
			chatParticipantsMap.put(session, subscribers);
		}
	}

	public void removeParticipant(String session, String name) {
		if (chatParticipantsMap.containsKey(session)) {
			Set<Participant> subscribers = chatParticipantsMap.get(session);
			Participant p = subscribers
					.stream()
					.filter(item -> item.getName().equals(name))
					.findFirst()
					.orElse(null);
			subscribers.remove(p);
			chatParticipantsMap.put(session, subscribers);
		}
	}

	/*public void broadcast() {
		if (messageQueue.isEmpty()) {
			System.out.println("no messages from publishers to display");
		} else {
			while (!messageQueue.isEmpty()) {
				Message message = messageQueue.remove();
				String session = message.getChat();

				Set<Participant> chatParticipants = chatParticipantsMap.get(session);

				for (Participant participant : chatParticipants) {
					participant.accept(message);
				}
			}
		}
	}

	public Set<Message> getMessagesForParticipantOf(String session, Participant participant) {
		if (messageQueue.isEmpty()) {
			System.out.println("no messages from publisters to display");
		} else {
			while (!messageQueue.isEmpty()) {
				Message message = messageQueue.remove();
				if (message.getChat().equalsIgnoreCase(session)) {
					Set<Participant> chatParticipants = chatParticipantsMap.get(session);

					for (Participant participant1 : chatParticipants) {
						if (participant1.equals(participant)) {
							participant.accept(message);
						}
					}
				}
			}
		}
		return participant.getSubscriberMessages();
	}*/

	public Set<Participant> getParticipantsOf(String chatSession) {
		if (chatParticipantsMap.containsKey(chatSession)) {
			return chatParticipantsMap.get(chatSession);
		}
		return new HashSet<>();
	}

	public Participant getParticipantInChatById(String chatName, Long id){
		Set<Participant> participants = getParticipantsOf(chatName);
		for(Participant participant : participants){
			if(participant.getId() == id){
				return participant;
			}
		}
		return null;
	}
}
