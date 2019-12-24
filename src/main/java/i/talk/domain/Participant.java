package i.talk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Setter
public class Participant {

    private Long id = 0L;

    private String imageUrl;

    private String name;

    @JsonIgnore
    private Set<Message> subscribedMessages = new HashSet<>();

    private LocalDateTime timeJoined;

    public Participant(Long id, String name) {
        this.id = id;
        this.name = name;
        this.timeJoined = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeJoined() {
        return timeJoined;
    }

    public Set<Message> getOwnMessages() {
        return subscribedMessages
                .stream()
                .filter(message -> message.getSender() == this)
                .collect(Collectors.toSet());
    }

    public LocalDateTime timeLastMessageWasSent() {
        try {
            long timeStamp = getOwnMessages().stream().mapToLong(Message::getTimeStamp).max().orElseThrow(RuntimeException::new);
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), TimeZone.getDefault().toZoneId());
        } catch (RuntimeException e) {
            return timeJoined;
        }
    }
}
