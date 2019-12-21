package i.talk.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import i.talk.domain.NameChangeResponse;
import i.talk.domain.Participant;
import i.talk.domain.enums.PictureUrlEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

	private Map<String, Set<Participant>> chatParticipantsMap = new HashMap<String, Set<Participant>>();

	@Autowired
	private SendingService sendingService;

	private Queue<Message> messageQueue = new LinkedList<Message>();

	public void joinChannel( String room, String name) {
		if (!isNameAlreadyTaken(room, name)) {
			Participant person = generateParticipant(room, name);
			addParticipant(room, person);
			sendingService.sendJoinedPersonAsObject(room, person);
			sendingService.sendHasJoinedMessageResponse(room, name);
		}
	}

	public void leaveChannel(@DestinationVariable String room, String name) {
		removeParticipant(room, name);
		sendingService.sendHasLeftMessageResponse(room, name);
	}

	public void deleteMessage(@DestinationVariable String room, String message) throws IOException {
		Message m = new ObjectMapper().readValue(message, Message.class);
		sendingService.sendDeleteResponse(room, m);
	}

	public void changeName(String room, String name, Long id) throws IOException{
		Participant p = getParticipantInChatById(room, id);
		String oldName = p.getName();
		removeParticipant(room, p.getName());
		p.setName(name);
		addParticipant(room, p);
		this.sendingService.sendHasChangedNameResponse(room, new NameChangeResponse(oldName, name));
	}

	public void addMessageToQueue(Message message) {
		messageQueue.add(message);
	}

	public boolean isNameAlreadyTaken(String room, String name){
		if(getParticipantsOf(room).stream().map(Participant::getName).collect(Collectors.toSet()).contains(name)){
			return true;
		}
		return false;
	}

	private Participant generateParticipant(String room, String name) {
		Long id = getFirstFreeIdIn(room);
		return generateParticipant(name, id);
	}

	public Participant generateParticipant(String name, Long id){
		Participant participant = new Participant(id, name);
		String imageUrl = PictureUrlEnums.getRandom().label;
		participant.setImageUrl(imageUrl);
		return participant;
	}

	public Long getFirstFreeIdIn(String chatName) {
		Set<Participant> participants = getParticipantsOf(chatName);
		Set<Long> ids = participants.stream().map(Participant::getId).collect(Collectors.toSet());
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

	public Set<Participant> getParticipantsOf(String chatSession) {
		if (chatParticipantsMap.containsKey(chatSession)) {
			return chatParticipantsMap.get(chatSession);
		}
		return new HashSet<>();
	}

	public Participant getParticipantInChatById(String chatName, Long id){
		Set<Participant> participants = getParticipantsOf(chatName);
		for(Participant participant : participants){
			if(participant.getId().equals(id)){
				return participant;
			}
		}
		return null;
	}
}
