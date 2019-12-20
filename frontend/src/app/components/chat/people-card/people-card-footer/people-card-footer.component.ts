import {Component, OnInit} from '@angular/core';
import {PeopleCardComponent} from "../people-card.component";
import {WebsocketService} from "../../../../services/websocket.service";
import {ChatService} from "../../../../services/chat.service";

@Component({
  selector: 'app-people-card-footer',
  templateUrl: './people-card-footer.component.html',
  styleUrls: ['./people-card-footer.component.css']
})
export class PeopleCardFooterComponent extends PeopleCardComponent implements OnInit {

  constructor(webSocketService: WebsocketService,
              chatService: ChatService) {
    super(webSocketService, chatService);
  }

  ngOnInit() {
  }

}
