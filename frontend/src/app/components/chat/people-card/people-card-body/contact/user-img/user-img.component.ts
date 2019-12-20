import {Component, Input, OnInit} from '@angular/core';
import {Person} from "../../../../../../model/person";
import {ContactComponent} from "../contact.component";
import {WebsocketService} from "../../../../../../services/websocket.service";
import {ChatService} from "../../../../../../services/chat.service";

@Component({
  selector: 'app-user-img',
  templateUrl: './user-img.component.html',
  styleUrls: ['./user-img.component.css']
})
export class UserImgComponent extends ContactComponent implements OnInit {

  @Input() contact: Person;

    constructor(webSocketService: WebsocketService,
                chatService: ChatService) {
        super(webSocketService, chatService);
    }

  ngOnInit() {
  }

}
