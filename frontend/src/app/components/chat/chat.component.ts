import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ChatSession} from "../../model/chatsession";
import {Person} from "../../model/person";
import {WebsocketService} from "../../services/websocket.service";
import {ChatService} from "../../services/chat.service";
import {Message} from "../../model/message";

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
                protected chatService: ChatService) {
    }

    ngOnInit() {
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
                        this.join(participantName, room)
                    });
                }
           }
            val.unsubscribe();
        })

    }

    private join(participantName: string, room: string) {
        this.webSocketService.join(participantName, room)
    }

    sendMessage(content) {
        let sender: Person =  new Person(this.getParticipant())
        let message = new Message(content, sender);
        this.webSocketService.sendMessage(JSON.stringify(message), ChatComponent.chat.name);
    }

    deleteMessage(message: Message) {
        this.webSocketService.delete(this.getChat().name, JSON.stringify(message));
    }

    getChat() {
        return ChatComponent.chat;
    }

    getParticipant() {
        return ChatComponent.participant;
    }

    getMessages() {
        return ChatComponent.participant.subscribedMessages;
    }


    parsetoDate(timestamp: number) {
        let date = new Date(timestamp)
        let seconds = date.getSeconds();
        let minutes = date.getMinutes();
        let hours = date.getHours();
        let day = date.getDay();
        let month = date.getMonth();
        let year = date.getFullYear();
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
