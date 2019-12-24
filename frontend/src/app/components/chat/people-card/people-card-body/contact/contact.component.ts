import {Component, Input, OnInit} from '@angular/core';
import {Person} from "../../../../../model/person";
import {PeopleCardBodyComponent} from "../people-card-body.component";
import {WebsocketService} from "../../../../../services/websocket.service";
import {ChatService} from "../../../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-contact',
    templateUrl: './contact.component.html',
    styleUrls: ['./contact.component.css']
})
export class ContactComponent extends PeopleCardBodyComponent implements OnInit {

    @Input() contact: Person;

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
    }

}
