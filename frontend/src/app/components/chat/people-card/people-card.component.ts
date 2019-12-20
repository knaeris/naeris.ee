import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ChatSession} from "../../../model/chatsession";
import {ChatComponent} from "../chat.component";
import {WebsocketService} from "../../../services/websocket.service";
import {ChatService} from "../../../services/chat.service";
import {AppComponent} from "../../../app.component";

@Component({
  selector: 'app-people-card',
  templateUrl: './people-card.component.html',
  styleUrls: ['./people-card.component.css']
})

export class PeopleCardComponent extends ChatComponent implements OnInit {

  constructor(webSocketService: WebsocketService,
               chatService: ChatService) {
    super(webSocketService,chatService);
  }

  ngOnInit() {
  }

}
