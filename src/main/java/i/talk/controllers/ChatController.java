package i.talk.controllers;

import i.talk.domain.Participant;
import i.talk.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("api/chat/{chatName}/participants")
    private ResponseEntity<Set<Participant>> getParticipant(@PathVariable String chatName) {
        Set<Participant> participants = this.chatService.getParticipantsOf(chatName);
        return ResponseEntity.ok().body(participants);
    }

    @GetMapping("api/chat/{chatName}/validation/{name}")
    private ResponseEntity<Boolean> nameValidation(@PathVariable String chatName, @PathVariable String name) {
        boolean nameTaken = chatService.isNameAlreadyTaken(chatName, name);
        return ResponseEntity.ok().body(nameTaken);
    }

    @GetMapping("api/chat/join-global")
    public ResponseEntity<String> joinGlobal() {
        String name = chatService.getGeneratedFreeNameInGlobal();
        return ResponseEntity.ok("{ \"name\" : \"" + name + "\" }");
    }

    @GetMapping("api/chat/{chatName}/callVoteKick/{kickedId}/{voteCallerId}")
    public ResponseEntity<Boolean> callVote(@PathVariable String chatName, @PathVariable Long kickedId, @PathVariable Long voteCallerId) {
        return ResponseEntity.ok(chatService.callVote(chatName, kickedId, voteCallerId));
    }
}
