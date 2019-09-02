package i.talk.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import i.talk.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {


	private final SimpMessagingTemplate template;

	@Autowired
	WebSocketController(SimpMessagingTemplate template){
		this.template = template;
	}

	@MessageMapping("send/message")
	public void onReceivedMessage(String message){
		ObjectMapper mapper = new ObjectMapper();
		Message messageObj;
		try {
			 messageObj = mapper.readValue(message, Message.class);
			 message =  mapper.writeValueAsString(messageObj);
		} catch (IOException e){
			System.out.println(e);
		}
		this.template.convertAndSend("/chat", message);
	}
}
