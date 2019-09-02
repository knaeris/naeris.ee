package i.talk;

import i.talk.domain.*;
import i.talk.domain.interfaces.Publisher;
import i.talk.services.PubSubService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TalkApplication {

	public static void main(String[] args) {

		SpringApplication.run(TalkApplication.class, args);
	}

}
