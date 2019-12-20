import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../../../../model/message";
import {CardBodyComponent} from "../card-body.component";
import {WebsocketService} from "../../../../../services/websocket.service";
import {ChatService} from "../../../../../services/chat.service";

@Component({
  selector: 'app-system-message',
  templateUrl: './system-message.component.html',
  styleUrls: ['./system-message.component.css']
})
export class SystemMessageComponent extends CardBodyComponent implements OnInit {

  @Input() message: Message;

  constructor(webSocketService: WebsocketService,
              chatService: ChatService) {
    super(webSocketService, chatService);
  }

  ngOnInit() {
  }

}
