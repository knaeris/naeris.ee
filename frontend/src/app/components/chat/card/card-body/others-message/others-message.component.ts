import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../../../../model/message";
import {CardBodyComponent} from "../card-body.component";
import {WebsocketService} from "../../../../../services/websocket.service";
import {ChatService} from "../../../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-others-message',
  templateUrl: './others-message.component.html',
  styleUrls: ['./others-message.component.css']
})
export class OthersMessageComponent extends CardBodyComponent implements OnInit {

  @Input() message: Message;

  constructor(webSocketService: WebsocketService,
              chatService: ChatService,
              router: Router) {
    super(webSocketService,chatService, router);

  }

  ngOnInit() {
  }

}
