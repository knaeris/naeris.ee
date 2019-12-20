import {Person} from "./person";
import {ChatComponent} from "../components/chat/chat.component";


export class ChatSession {

    name: string;

    participants: Person[] = [];


    constructor(name: string) {
        this.name = name;
    }
}