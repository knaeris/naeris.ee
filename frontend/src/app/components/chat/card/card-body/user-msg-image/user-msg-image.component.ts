import {Component, Input, OnInit} from '@angular/core';
import {Person} from "../../../../../model/person";
import {CardBodyComponent} from "../card-body.component";
import {WebsocketService} from "../../../../../services/websocket.service";
import {ChatService} from "../../../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-user-msg-image',
    templateUrl: './user-msg-image.component.html',
    styleUrls: ['./user-msg-image.component.css']
})
export class UserMsgImageComponent extends CardBodyComponent implements OnInit {

    @Input() participant: Person;

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
    }

}
