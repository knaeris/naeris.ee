import {Component, Input, OnInit} from '@angular/core';
import {ChatSession} from "../../../../model/chatsession";
import {PeopleCardComponent} from "../people-card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";
import {ChatComponent} from "../../chat.component";
import {Person} from "../../../../model/person";

@Component({
  selector: 'app-people-card-body',
  templateUrl: './people-card-body.component.html',
  styleUrls: ['./people-card-body.component.css']
})
export class PeopleCardBodyComponent extends PeopleCardComponent implements OnInit {

  constructor(webSocketService: WebsocketService,
              chatService: ChatService) {
    super(webSocketService, chatService);
  }

  ngOnInit() {
  }


}
