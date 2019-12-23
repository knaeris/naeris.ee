import {Injectable} from "@angular/core";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {Operationenum} from "../model/operationenum";
import {Message} from "../model/message";
import {Person} from "../model/person";
import {ChatService} from "./chat.service";
import {ChatSession} from "../model/chatsession";
import {ResponseReader} from "../model/responseReader";
import {KickVotePoll, Vote} from "../model/kickvotepoll";
import {killLocalSessionSharedService, LeavingService} from "./leavingService";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable()
export class WebsocketService {
   // private serverUrl = 'http://134.209.21.45:8080/talk-0.0.1-SNAPSHOT/socket';
    private serverUrl = 'http://localhost:8080/socket';
    private stompClient;
    private channel = "/chat/";
    public isConnected = new BehaviorSubject<boolean>(true);

    constructor(private chatService: ChatService) {
    }

     connects(room: ChatSession, participant: Person, callback){
        let ws = new SockJS(this.serverUrl);
        this.stompClient = Stomp.over(ws);
        //this.stompClient.debug = null
        const that = this;
        this.stompClient.connect({}, (frame) => {
            that.stompClient.subscribe(that.channel + room.name, (message) => {
                that.isConnected.next(true);
                that.processResponsesByOperation(new ResponseReader(message.body), room, participant);
            });
            if(callback){
                callback();
            }
        }, this.errorCb);
    };

    processResponsesByOperation(response: ResponseReader, room: ChatSession, currentUser: Person) {
        console.log("Message Recieved from Server :: " + response);
        switch (response.operation) {
            case Operationenum.JOIN:
                this.initCurrentUser(currentUser, response);
                this.populateParticipantsOf(room);
                break;
            case Operationenum.SEND :
                this.convertResponseToSystemMessageForSending(response, currentUser);
                break;
            case Operationenum.DELETE:
                this.deleteMessage(response,currentUser);
                break;
            case Operationenum.LEAVE :
                if(currentUser && response.payload && currentUser.id == response.payload.id){
                    this.isConnected.next(false);
                }
                this.populateParticipantsOf(room);
                break;
            case Operationenum.CHANGE :
                if(currentUser.name == response.payload.oldName){
                    currentUser.name = response.payload.newName;
                }
                this.populateParticipantsOf(room);
                break;
            case Operationenum.VOTE :
                    let poll: KickVotePoll = response.getPayLoad();
                    room.activePoll = poll;

                break;
        }
    }

    private convertResponseToSystemMessageForSending(response: ResponseReader, currentUser: Person) {
        let message: Message = this.convertToMessage(response);
        currentUser.subscribedMessages.push(message);
    }

    private initCurrentUser(currentUser: Person, wsMessage: ResponseReader) {
        if (!currentUser.id) {
            let person: Person = wsMessage.payload;
            currentUser.name = person.name;
            currentUser.id = person.id;
            currentUser.imageUrl = person.imageUrl;
        }
    }

    private convertToMessage(response: ResponseReader): Message {
            let message: Message = new Message(response.payload.payload, response.payload.sender);
            response.payload.timeStamp ? message.timeStamp = response.payload.timeStamp: (doNothing) => ({});
            return message;
        }


    private deleteMessage(wsMessage: ResponseReader, participant: Person){
        let message: Message = wsMessage.payload as Message;
        participant.subscribedMessages = participant.subscribedMessages.filter(m => m.timeStamp != message.timeStamp);
    }

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
    }

    errorCb(error, room) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            // this.connect(room);
        }, 5000);
    }

    sendMessage(message: Message, room: string) {
        this.stompClient.send("/app/send/" + room, {}, JSON.stringify(message));
    }

    join(name: string, room: string) {
        this.stompClient.send("/app/join/" + room, {}, name);
    }

    leave(room: string, name: string) {
        this.stompClient.send("/app/leave/" + room, {}, name);
    }

    delete(room: string, message: Message){
        this.stompClient.send("/app/delete/" + room, {}, JSON.stringify(message));
    }

    changeName(room: string, name: string, id: number){
        this.stompClient.send("/app/changeName/" + room + "/" + name, {}, id)
    }

    vote(room: string, vote: Vote){
        this.stompClient.send("/app/vote/" + room, {}, JSON.stringify(vote));
    }

    private populateParticipantsOf(room) {
        let ppl = this.chatService.getParticipantsOf(room.name).subscribe(value => {
            room.participants = value;
            ppl.unsubscribe();
        });
    }
}