import {Component, OnInit} from '@angular/core';
import {ChatService} from "../../../../services/chat.service";
import {WebsocketService} from "../../../../services/websocket.service";
import {PeopleCardComponent} from "../people-card.component";
import {Router} from "@angular/router";

@Component({
    selector: 'app-people-card-header',
    templateUrl: './people-card-header.component.html',
    styleUrls: ['./people-card-header.component.css']
})
export class PeopleCardHeaderComponent extends PeopleCardComponent implements OnInit {

    personName: string = "";
    chatName: string = "";

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
    }

    joinChat() {
        this.chatName.trim();
        this.personName.trim()
        if (this.chatName != "" && this.personName != "") {
            super.joinChat(this.chatName, this.personName);
            this.chatName = "";
            this.personName = "";
        }
    }

    changeName() {
        super.changeName(this.personName);
        this.personName = "";

    }
}
