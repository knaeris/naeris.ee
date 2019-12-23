import {Person} from "./person";

export class Vote {
    value: boolean;
    voterId: number;


    constructor(value: boolean, voterId: number) {
        this.value = value;
        this.voterId = voterId;
    }
}


export class KickVotePoll{
    voteCaller: Person;
    personToKick: Person;
    chatName: string;
    positiveVotesNeeded: number;
    votes: Vote[];
    timeToVote: number;

}