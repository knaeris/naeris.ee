import {Message} from "./message";
import {Operationenum} from "./operationenum";
import {Websocketmessage} from "./websocketmessage";


export class Person implements Websocketmessage{

    id: number;

    name: string;

    imageUrl: string;

    subscribedMessages: Message[] = [];

    constructor(person: any){
        if(person){
            this.id = person.id;
            this.name = person.name;
            this.imageUrl = person.imageUrl;
        }
    }
}