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

	@GetMapping("api/chat/{chatName}/participants")
	private ResponseEntity<?> getParticipant(@PathVariable String chatName){
		Set<Participant> participants = this.chatService.getParticipantsOf(chatName);
		return ResponseEntity.ok().body(participants);
	}
}
