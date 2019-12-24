import {Component, OnInit} from '@angular/core';
import {ChatComponent} from "../chat.component";
import {WebsocketService} from "../../../services/websocket.service";
import {ChatService} from "../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-people-card',
    templateUrl: './people-card.component.html',
    styleUrls: ['./people-card.component.css']
})

export class PeopleCardComponent extends ChatComponent implements OnInit {

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
    }

}
