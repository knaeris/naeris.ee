package i.talk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KickVotePoll {

    private Participant voteCaller;
    private Participant personToKick;
    private String chatName;
    private int positiveVotesNeeded;
    private List<Vote> votes;
    private int timeToVote;

    public KickVotePoll(Participant personToKick, Participant voteCaller, String chatName, int positiveVotesNeeded) {
        this.voteCaller = voteCaller;
        this.personToKick = personToKick;
        this.chatName = chatName;
        this.positiveVotesNeeded = positiveVotesNeeded;
        this.votes = new ArrayList<>();
        this.timeToVote = 60;
    }

    public int getNumberOfPositiveVotes() {
        return (int) votes.stream().filter(Vote::isValue).count();
    }
}
