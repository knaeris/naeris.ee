import {Component, OnInit} from '@angular/core';
import {CardComponent} from "../card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-card-body',
    templateUrl: './card-body.component.html',
    styleUrls: ['./card-body.component.css']
})
export class CardBodyComponent extends CardComponent implements OnInit {

    global: string = "#global";

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);

    }

    ngOnInit() {
    }

}
