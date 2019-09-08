package i.talk.services;

import i.talk.domain.Message;
import i.talk.domain.Participant;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PubSubService {

	Map<String, Set<Participant>> chatParticipantsMap = new HashMap<String, Set<Participant>>();

	Queue<Message> messageQueue = new LinkedList<Message>();

	public void addMessageToQueue(Message message) {
		messageQueue.add(message);
	}

	public void addParticipant(String session, Participant participant) {
		if (chatParticipantsMap.containsKey(session)) {
			Set<Participant> subscribers = chatParticipantsMap.get(session);
			Set<String> subscribersNames = subscribers.stream().map(x -> x.getName()).collect(Collectors.toSet());
			subscribers.add(participant);
			chatParticipantsMap.put(session, subscribers);
		} else {
			Set<Participant> subscribers = new HashSet<Participant>();
			subscribers.add(participant);
			chatParticipantsMap.put(session, subscribers);
		}
	}

	public void removeParticipant(String session, Participant participant) {
		if (chatParticipantsMap.containsKey(session)) {
			Set<Participant> subscribers = chatParticipantsMap.get(session);
			subscribers.remove(participant);
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
