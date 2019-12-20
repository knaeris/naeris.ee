import {Component, OnInit} from '@angular/core';
import {CardComponent} from "../card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";

@Component({
    selector: 'app-card-header',
    templateUrl: './card-header.component.html',
    styleUrls: ['./card-header.component.css']
})
export class CardHeaderComponent extends CardComponent implements OnInit {

    constructor(webSocketService: WebsocketService,
                chatService: ChatService) {
        super(webSocketService, chatService);
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
