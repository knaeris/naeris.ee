package i.talk.controllers;

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

	@PostMapping("api/chat/{chatName}/send")
	public ResponseEntity<?> send(@PathVariable String chatName, @RequestBody Message message, @RequestHeader Map<String, String> headers){
		Long id = Long.parseLong(headers.get("x-id"));
		Participant sender = pubSubService.getParticipantInChatById(chatName, id);
		//sender.send(message, pubSubService);
		return ResponseEntity.ok()
				.body(message);
	}
	@GetMapping("api/chat/{chatName}/participants")
	private ResponseEntity<?> getParticipant(@PathVariable String chatName){
		Set<Participant> participants = this.pubSubService.getParticipantsOf(chatName);
		return ResponseEntity.ok().body(participants);
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
