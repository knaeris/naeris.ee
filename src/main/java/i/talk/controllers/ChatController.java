package i.talk.controllers;

import i.talk.domain.ChatSession;
import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.domain.enums.PictureUrlEnums;
import i.talk.services.PubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ChatController {

	@Autowired
	private PubSubService pubSubService;

	@PostMapping("api/chat/{chatName}")
	public ResponseEntity<?> join(@PathVariable String chatName, @RequestBody String personName){
		Long id = getFirstFreeIdIn(chatName);
		Participant participant = new Participant(id, personName);
		if(pubSubService.getParticipantsOf(chatName).stream().map(x->x.getName()).collect(Collectors.toSet()).contains(personName)){
			return ResponseEntity.badRequest().body(null);
		}
		ChatSession session = participant.join(chatName, pubSubService);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-ID", id.toString());
		ResponseEntity<?> kk = ResponseEntity.ok()
				.headers(responseHeaders)
				.body(session);
		return kk;
	}

	@PutMapping("api/chat/{chatName}/changename/{id}")
	public ResponseEntity<ChatSession> changeName(@PathVariable String chatName, @RequestBody String newName, @PathVariable Long id){
		Participant participant = pubSubService.getParticipantInChatById(chatName, id);
		participant.changeNameTo(newName, chatName, pubSubService);
		Set<Participant> participants = pubSubService.getParticipantsOf(chatName);
		ChatSession session = new ChatSession(chatName);
		session.setParticipants(participants);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-ID", participant.getId().toString());
		responseHeaders.set("X-Name", participant.getName());
		return ResponseEntity.ok()
				.headers(responseHeaders)
				.body(session);
	}

	@PostMapping("api/chat/{chatName}/send")
	public ResponseEntity<?> send(@PathVariable String chatName, @RequestBody Message message, @RequestHeader Map<String, String> headers){
		Long id = Long.parseLong(headers.get("x-id"));
		Participant sender = pubSubService.getParticipantInChatById(chatName, id);
		//sender.send(message, pubSubService);
		return ResponseEntity.ok()
				.body(message);
	}
	@GetMapping("api/chat/{chatName}/participants")
	private ResponseEntity<?> getParticipantIds(@PathVariable String chatName){
		return ResponseEntity.ok().body(this.pubSubService.getParticipantsOf(chatName));
	}
	private Long getFirstFreeIdIn(String chatName) {
		Set<Long> ids = getAllParticipantIdsInChat(chatName);

		Long id = 1L;
		while(ids.contains(id)){
			id++;
		}
		return id;
	}

	private Set<Long> getAllParticipantIdsInChat(String chatName) {
		return pubSubService.getParticipantsOf(chatName)
					.stream()
					.map(x -> x.getId())
					.collect(Collectors.toSet());
	}

}
