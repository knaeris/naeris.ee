import {AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild} from '@angular/core';
import {CardComponent} from "../card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";

@Component({
  selector: 'app-card-body',
  templateUrl: './card-body.component.html',
  styleUrls: ['./card-body.component.css']
})
export class CardBodyComponent extends CardComponent implements OnInit {

  constructor(webSocketService: WebsocketService,
              chatService: ChatService) {
    super(webSocketService, chatService);
  }

  ngOnInit() {
  }

}
