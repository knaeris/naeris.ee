package i.talk;

import i.talk.domain.Message;
import i.talk.domain.Participant;
import i.talk.services.PubSubService;
import org.junit.Test;

import javax.servlet.http.Part;
import javax.validation.constraints.AssertTrue;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ChatTest {

    @Test
    public void canCreateANewSession(){
        Participant person = new Participant("dad");
        PubSubService pubSubService = new PubSubService();
        String session = "haha";
        person.join(session,pubSubService);
        assertThat(pubSubService.getParticipantsOf(session).size(), is(1));
    }

    @Test
    public void canCreateMultipleSessionsSameTime(){
        PubSubService pubSubService = new PubSubService();
        Participant person = new Participant("dada");
        String session = "hans";
        Participant person1 = new Participant("da");
        String session1 = "dada";
        person.join(session, pubSubService);
        person1.join(session1, pubSubService);
        assertTrue(pubSubService.getParticipantsOf(session).contains(person));
        assertFalse(pubSubService.getParticipantsOf(session).contains(person1));
        assertTrue(pubSubService.getParticipantsOf(session1).contains(person1));
        assertFalse(pubSubService.getParticipantsOf(session1).contains(person));
    }
    @Test
    public void canMultiplePeopleJoin(){
        Participant person = new Participant("dsad");
        Participant person1 = new Participant("plf");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session,pubSubService);
        person1.join(session, pubSubService);
        assertThat(pubSubService.getParticipantsOf(session).size(), is(2));
    }

    @Test
    public void canSendAMessage(){
        Participant person = new Participant("ausa");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session,pubSubService);
        Message message = new Message(session,"daun");
        person.send(message,pubSubService);
        assertThat(person.getMessagesForParticipantOf(session,pubSubService).size(), is(1));
    }

    @Test
    public void canSendMultipleMessages(){
        Participant person = new Participant("jarmo");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session,pubSubService);
        Message message = new Message(session,"daun");
        Message message1 = new Message(session,"jaja");
        Message message2 = new Message(session,"maun");
        person.send(message,pubSubService);
        person.send(message1,pubSubService);
        person.send(message2,pubSubService);
        assertThat(person.getMessagesForParticipantOf(session,pubSubService).size(), is(3));
    }

    @Test
    public void canLeaveTheChatSession(){
        Participant person = new Participant("dad");
        Participant person1 = new Participant("gfh");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session,pubSubService);
        person1.join(session,pubSubService);
        person.leave(session, pubSubService);
        assertFalse(pubSubService.getParticipantsOf(session).contains(person));
    }

    @Test
    public void canRemoveAMessage(){
        Participant person = new Participant("dfgfg");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session, pubSubService);
        Message message1 = new Message(session,"456");
        Message message2 = new Message(session,"878");
        Message message = new Message(session,"878888");
        person.send(message, pubSubService);
        person.send(message1, pubSubService);
        person.send(message2,pubSubService);
        person.remove(message1,pubSubService);
        assertTrue(person.getSubscriberMessages().contains(message));
        assertTrue(person.getSubscriberMessages().contains(message2));
        assertFalse(person.getSubscriberMessages().contains(message1));
    }

    @Test
    public void canRemoveMultipleMessages(){
        Participant person = new Participant("dsads");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session, pubSubService);
        Message message1 = new Message(session,"456");
        Message message2 = new Message(session,"878");
        Message message = new Message(session,"878888");
        person.send(message, pubSubService);
        person.send(message1, pubSubService);
        person.send(message2,pubSubService);
        person.remove(message1,pubSubService);
        person.remove(message,pubSubService);
        person.remove(message2,pubSubService);
        assertTrue(person.getSubscriberMessages().isEmpty());
    }

    @Test
    public void differentPeopleInSameChatCanSeeSameMessages(){
        Participant person = new Participant("kjsova");
        Participant person1 = new Participant("gssdf");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session, pubSubService);
        person1.join(session, pubSubService);
        Message message1 = new Message(session,"456");
        Message message2 = new Message(session,"878");
        Message message = new Message(session,"878888");
        person.send(message,pubSubService);
        person1.send(message1, pubSubService);
        person.send(message2, pubSubService);
        assertThat(person.getSubscriberMessages().size(), is(3));
        assertEquals(person.getSubscriberMessages(),person1.getSubscriberMessages());
    }
    @Test
    public void differentPeopleInSameChatCanSeeSameMessagesAfterRemoval(){
        Participant person = new Participant("ggth");
        Participant person1 = new Participant("dsad");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session, pubSubService);
        person1.join(session, pubSubService);
        Message message1 = new Message(session,"456");
        Message message2 = new Message(session,"878");
        Message message = new Message(session,"878888");
        person.send(message,pubSubService);
        person1.send(message1, pubSubService);
        person.send(message2, pubSubService);
        person.remove(message, pubSubService);
        person.remove(message1, pubSubService);
        assertThat(person.getSubscriberMessages().size(), is(1));
        assertEquals(person.getSubscriberMessages(),person1.getSubscriberMessages());
    }

    @Test void canChangeName(){
        Participant person = new Participant("ggth");
        Participant person1 = new Participant("dsad");
        PubSubService pubSubService = new PubSubService();
        String session = "hans";
        person.join(session, pubSubService);
        person1.join(session, pubSubService);
        person.changeNameTo("aadu", session, pubSubService);
        assertEquals(person.getName(),"aadu");
        assertEquals(person1.getName(), "dsad");
    }

//vaatad unit teste ja katsud lihtsutada. kui liiga raske siis lihtsusta edasi


}