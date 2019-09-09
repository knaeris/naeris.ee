package i.talk.services;

import i.talk.domain.Message;
import i.talk.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidateService {

    @Autowired
    private MessageService messageService;
    @Autowired
    private ChatService chatService;

    public void validateJoinRequest(String room, String joinedMessage) throws IOException {
        String name = messageService.getPayload(joinedMessage);
        Set<String> names = chatService.getParticipantsOf(room)
                .stream()
                .map(Participant::getName)
                .collect(Collectors.toSet());
        if(names.contains(name)){
            throw new IOException();
        }
    }
}
