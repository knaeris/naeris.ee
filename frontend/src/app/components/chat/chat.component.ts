import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ChatSession} from "../../model/chatsession";
import {Person} from "../../model/person";
import {WebsocketService} from "../../services/websocket.service";
import {ChatService} from "../../services/chat.service";
import {Message} from "../../model/message";
import {Vote} from "../../model/kickvotepoll";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css'],
    //encapsulation: ViewEncapsulation.None // <------
})
export class ChatComponent implements OnInit, OnDestroy {

    static chat: ChatSession;
    static participant: Person;
    static hasLeft: boolean = false;

    constructor(protected webSocketService: WebsocketService,
                protected chatService: ChatService,
                protected router: Router) {
    }

    ngOnInit() {
        if(this.router.url) {
            if (this.router.url == '/global') {
                this.joinGlobal();
                return;
            }
            let url: string = this.router.url.substr(1);
            let roomAndName: string[] = url.split("/");
            let room = roomAndName[0];
            let name = roomAndName[1];
            this.joinChat(room, name);
        }
    }

    ngOnDestroy(): void {
    }

    joinChat(room: string, participantName: string) {
        this.leave();
        let val = this.chatService.validateName(room, participantName)
            .subscribe(nameTaken => {
            if (nameTaken) {
                alert(participantName + " nimi on juba ruumis " + room + " hõivatud");
            } else {
                if (!ChatComponent.chat || (ChatComponent.chat && ChatComponent.chat.name != room)) {
                    ChatComponent.chat = new ChatSession(room);
                    ChatComponent.participant = new Person("");
                    this.webSocketService.connects(ChatComponent.chat, ChatComponent.participant, () => {
                        this.join(participantName, room);
                    });
                }
           }
            val.unsubscribe();
        });

        let val2 =  this.webSocketService.isConnected.subscribe(value => {
            if(value == false){
                this.webSocketService.disconnect();
                this.killLocalSession();
                if(val2){
                    val2.unsubscribe();
                }
            }
        })
    }

    private join(participantName: string, room: string) {
        this.webSocketService.join(participantName, room)
    }

    sendMessage(content) {
        let sender: Person =  new Person(this.getParticipant())
        let message = new Message(content, sender);
        this.webSocketService.sendMessage(message, ChatComponent.chat.name);
    }

    deleteMessage(message: Message) {
        this.webSocketService.delete(this.getChat().name, message);
    }

    getChat() {
        return ChatComponent.chat;
    }

    getPositiveVotesSize(){
        return this.getChat().activePoll.votes.filter(val => val.value == true).length
    }

    getParticipant() {
        return ChatComponent.participant;
    }

    getMessages() {
        return ChatComponent.participant.subscribedMessages;
    }

    callVoteKick(person: Person) {
        this.chatService.callVoteKick(person.id, this.getChat().name, this.getParticipant().id)
            .subscribe(canCallVote => {
                if(canCallVote){
                    this.vote(true);
                }
            }
        )
    }

    isVoted(): boolean{
        return this.getChat().activePoll.votes.map(vote => vote.voterId).indexOf(this.getParticipant().id) > -1;
    }

    vote(value: boolean){
        this.webSocketService.vote(this.getChat().name, new Vote(value, this.getParticipant().id));
    }

    joinGlobal(){
        let val =  this.chatService.joinGlobal()
            .subscribe(r => {
                r = JSON.stringify(r)
                let obj  = JSON.parse(r);
                console.log(obj.name)
            this.joinChat("global", obj.name);
            val.unsubscribe();
        })
    }

    parsetoDate(timestamp: number) {
        let date = new Date(timestamp)
        let seconds = date.getSeconds();
        let minutes = date.getMinutes();
        let hours = date.getHours();
        return hours.toString().padStart(2, "0") + ":"
            + minutes.toString().padStart(2, "0") + ":"
            + seconds.toString().padStart(2, "0");
    }

    leave() {
        if (this.getChat() && this.getParticipant()) {
            let payload: string = this.getParticipant().name;
            this.webSocketService.leave(this.getChat().name, payload);
           this.webSocketService.disconnect();
            this.killLocalSession();
        }
    }

    private killLocalSession() {
        ChatComponent.chat = null;
        ChatComponent.participant = null;
    }

    @HostListener('window:beforeunload')
    private leaveChatAfterLeavingPage() {
        if (!ChatComponent.hasLeft) {
            this.leave();
            ChatComponent.hasLeft = true;
        }
    }

    changeName(name: string){
        let val = this.chatService.validateName(this.getChat().name, name)
            .subscribe(nameTaken => {
                if(nameTaken){
                    alert(name + " juba eksisteerib ruumis")
                } else {
                    this.webSocketService.changeName(this.getChat().name, name, this.getParticipant().id);
                }
                val.unsubscribe();
            } )
    }

}
