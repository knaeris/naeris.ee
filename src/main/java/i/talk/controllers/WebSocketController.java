package i.talk.controllers;

import i.talk.services.ChatService;
import i.talk.services.ResponseService;
import i.talk.services.SendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    private ChatService chatService;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("send/{room}")
    public void onReceivedMessage(@DestinationVariable String room, String message) throws IOException {
        this.template.convertAndSend("/chat/" + room, timeStamp(message));
    }

    @MessageMapping("join/{room}")
    public void joinChannel(@DestinationVariable String room, String name) {
        chatService.joinChannel(room,name);
    }

    @MessageMapping("leave/{room}")
    public void leaveChannel(@DestinationVariable String room, String name) {
        chatService.leaveChannel(room, name);
    }

    @MessageMapping("delete/{room}")
    public void deleteMessage(@DestinationVariable String room, String message) throws IOException {
        chatService.deleteMessage(room,message);
    }

    @MessageMapping("changeName/{room}/{name}")
    public void changeName(@DestinationVariable String room, @DestinationVariable String name, Long id) throws IOException{
        chatService.changeName(room, name, id);
    }

    @MessageMapping("vote/{room}")
    public void vote(@DestinationVariable String room, String vote) {
        chatService.vote(room, vote);
    }

    private String timeStamp(String message) throws IOException {
        return ResponseService.timeStamp(message);
    }

}
