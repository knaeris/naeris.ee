package i.talk.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.*;
import i.talk.domain.enums.PictureUrlEnums;
import i.talk.domain.exceptions.ActivePollExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

	private Map<String, Set<Participant>> chatParticipantsMap = new HashMap<String, Set<Participant>>();

	private List<KickVotePoll> allActiveKickVotePolls = new ArrayList<>();

	@Autowired
	private SendingService sendingService;

	private Queue<Message> messageQueue = new LinkedList<Message>();

	public String getGeneratedFreeNameInGlobal(){
		String room = "global";
		String name;
		int i = 0;
		do {
			name = "user";
			name += i++;
		}while(isNameAlreadyTaken(room, name));

		return name;
	}

	public void joinChannel( String room, String name) {
		if (!isNameAlreadyTaken(room, name)) {
			Participant person = generateParticipant(room, name);
			addParticipant(room, person);
			sendingService.sendJoinedPersonAsObject(room, person);
			sendingService.sendHasJoinedSystemMessage(room, name);
			if(hasActiveKickVotePoll(room)) {
				sendingService.sendVoteResponse(room, getPoll(room));
			}
		}
	}

	public void leaveChannel(String room, String name) {
		removeParticipant(room, name);
		sendingService.sendHasLeftMessageSystemMessage(room, name);
		Participant p = getParticipantsOf(room).stream().filter(person -> person.getName().equals(name)).findFirst().orElse(null);
		sendingService.sendSomeOneHasLeftOrKickedResponse(room, p);
	}

	public void deleteMessage(String room, String message) throws IOException {
		Message m = new ObjectMapper().readValue(message, Message.class);
		sendingService.sendDeleteResponse(room, m);
	}

	public void changeName(String room, String name, Long id) {
		Participant p = getParticipantInChatById(room, id);
		String oldName = p.getName();
		removeParticipant(room, p.getName());
		p.setName(name);
		addParticipant(room, p);
		NameChangeResponse r = new NameChangeResponse(oldName, name);
		this.sendingService.sendHasChangedNameResponse(room, r);
		this.sendingService.sendHasChangedNameSystemMessage(room, r);
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

	public boolean callVote(String chatName, Long kickedId, Long voteCallerId){
		if(hasActiveKickVotePoll(chatName)){
		    throw new ActivePollExistsException();
		} else {
			Participant personToKick = getParticipantInChatById(chatName, kickedId);
			Participant voteCaller = getParticipantInChatById(chatName, voteCallerId);
			int numberOfParticipants = getParticipantsOf(chatName).size();
			KickVotePoll poll = new KickVotePoll(personToKick, voteCaller, chatName, numberOfParticipants / 2 + 1);
			allActiveKickVotePolls.add(poll);
			sendingService.sendHasCalledAVoteSystemMessage(poll);
			return true;
		}

	}

	private boolean hasActiveKickVotePoll(String chatName) {
		return allActiveKickVotePolls.stream().anyMatch(o -> o.getChatName().equals(chatName));

	}

	private KickVotePoll getPoll(String room){
		return allActiveKickVotePolls.stream().filter(poll -> poll.getChatName().equals(room)).findFirst().orElse(null);
	}

	public void vote(String room, String vote) {
		try {
		KickVotePoll poll = getPoll(room);
		Vote voteObject = new ObjectMapper().readValue(vote, Vote.class);
		Long voterId = voteObject.getVoterId();
		boolean hasAlreadyVoted = poll.getVotes().stream().anyMatch(v -> v.getVoterId().equals(voterId));
		if(hasAlreadyVoted){
			return;
		}
		poll.getVotes().add(voteObject);
		sendingService.sendVoteResponse(room, poll);


		if(poll.getNumberOfPositiveVotes() == poll.getPositiveVotesNeeded() || poll.getVotes().size() == getParticipantsOf(poll.getChatName()).size()) {
			allActiveKickVotePolls.remove(poll);
			sendingService.sendVoteResponse(room, null);
		}

		if(poll.getNumberOfPositiveVotes() == poll.getPositiveVotesNeeded()){
				Participant kickedPerson = poll.getPersonToKick();
				kick(room, kickedPerson);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void kick(String room, Participant kickedPerson) {
		sendingService.sendSomeOneHasLeftOrKickedResponse(room, kickedPerson);
		sendingService.sendHasKickedFromRoomSystemMessage(room, kickedPerson.getName());
		removeParticipant(room, kickedPerson.getName());
	}

}
