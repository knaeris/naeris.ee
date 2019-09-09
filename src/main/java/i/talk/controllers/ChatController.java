package i.talk.controllers;

import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ChatController {

	@Autowired
	private ChatService chatService;

	@PostMapping("api/chat/{chatName}/send")
	public ResponseEntity<?> send(@PathVariable String chatName, @RequestBody Message message, @RequestHeader Map<String, String> headers){
		Long id = Long.parseLong(headers.get("x-id"));
		Participant sender = chatService.getParticipantInChatById(chatName, id);
		//sender.send(message, pubSubService);
		return ResponseEntity.ok()
				.body(message);
	}
	@GetMapping("api/chat/{chatName}/participants")
	private ResponseEntity<?> getParticipant(@PathVariable String chatName){
		Set<Participant> participants = this.chatService.getParticipantsOf(chatName);
		return ResponseEntity.ok().body(participants);
	}

	private Set<Long> getAllParticipantIdsInChat(String chatName) {
		return chatService.getParticipantsOf(chatName)
					.stream()
					.map(x -> x.getId())
					.collect(Collectors.toSet());
	}

}
