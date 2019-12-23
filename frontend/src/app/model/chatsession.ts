import {Person} from "./person";
import {ChatComponent} from "../components/chat/chat.component";
import {KickVotePoll} from "./kickvotepoll";


export class ChatSession {

    name: string;

    participants: Person[] = [];

    activePoll: KickVotePoll = null;


    constructor(name: string) {
        this.name = name;
    }
}