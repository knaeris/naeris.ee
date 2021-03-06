import {Component, OnInit} from '@angular/core';
import {PeopleCardComponent} from "../people-card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-people-card-body',
    templateUrl: './people-card-body.component.html',
    styleUrls: ['./people-card-body.component.css']
})
export class PeopleCardBodyComponent extends PeopleCardComponent implements OnInit {

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
    }
}
