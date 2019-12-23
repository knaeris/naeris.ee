import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Message} from "../../../../../model/message";
import {CardBodyComponent} from "../card-body.component";
import {WebsocketService} from "../../../../../services/websocket.service";
import {ChatService} from "../../../../../services/chat.service";
import {AppComponent} from "../../../../../app.component";
import {ChatComponent} from "../../../chat.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";

@Component({
  selector: 'app-my-message',
  templateUrl: './my-message.component.html',
  styleUrls: ['./my-message.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MyMessageComponent extends CardBodyComponent implements OnInit {

  @Input() message: Message;

  constructor(webSocketService: WebsocketService,
              chatService: ChatService,
              router: Router) {
    super(webSocketService,chatService, router);
  }

  ngOnInit() {
  }

  delete(){
    super.deleteMessage(this.message);
  }
}
