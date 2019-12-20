import {Component, Input, OnInit} from '@angular/core';
import {Person} from "../../../../../../model/person";
import {ContactComponent} from "../contact.component";
import {WebsocketService} from "../../../../../../services/websocket.service";
import {ChatService} from "../../../../../../services/chat.service";

@Component({
  selector: 'app-contact-info',
  templateUrl: './contact-info.component.html',
  styleUrls: ['./contact-info.component.css']
})
export class ContactInfoComponent extends ContactComponent implements OnInit {

  @Input() contact: Person;

  constructor(webSocketService: WebsocketService,
              chatService: ChatService) {
    super(webSocketService, chatService);
  }

  ngOnInit() {
  }

}
