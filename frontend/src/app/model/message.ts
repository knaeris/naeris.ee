import {Person} from "./person";
import {Websocketmessage} from "./websocketmessage";


export class Message implements Websocketmessage {

    payload: string;
    sender: Person;
    timeStamp: number;

    constructor(content: string, sender: Person) {
        this.payload = content;
        this.sender = sender;
    }

}