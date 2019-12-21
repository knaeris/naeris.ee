package i.talk.controllers;

import i.talk.domain.Participant;
import i.talk.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ChatController {

	@Autowired
	private ChatService chatService;

	@GetMapping("api/chat/{chatName}/participants")
	private ResponseEntity<Set<Participant>> getParticipant(@PathVariable String chatName){
		Set<Participant> participants = this.chatService.getParticipantsOf(chatName);
		return ResponseEntity.ok().body(participants);
	}

	@GetMapping("api/chat/{chatName}/validation/{name}")
    private ResponseEntity<Boolean> nameValidation(@PathVariable String chatName, @PathVariable String name){
	    boolean nameTaken = chatService.isNameAlreadyTaken(chatName, name);
	    return ResponseEntity.ok().body(nameTaken);
    }

	@GetMapping("api/chat/join-global")
	public ResponseEntity<String> joinGlobal() {
		String room = "global";
		String name;
		int i = 0;
		do {
			name = "user";
			name += i++;
		}while(chatService.isNameAlreadyTaken(room, name));

		return ResponseEntity.ok("{ \"name\" : \"" + name + "\" }");
	}
}
