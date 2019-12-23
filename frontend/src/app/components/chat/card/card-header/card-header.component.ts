import {Component, OnInit} from '@angular/core';
import {CardComponent} from "../card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-card-header',
    templateUrl: './card-header.component.html',
    styleUrls: ['./card-header.component.css']
})
export class CardHeaderComponent extends CardComponent implements OnInit {

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
    }

    getParticipantNumber(): string {
        if (this.getChat() && this.getChat().participants) {
            let number = this.getChat().participants.length;
            return number != 1 ? number + " osalejat" : number + " osaleja";
        }
    }
}
