import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CardFooterComponent} from "../card-footer.component";
import {WebsocketService} from "../../../../../services/websocket.service";
import {ChatService} from "../../../../../services/chat.service";

@Component({
  selector: 'app-emoji',
  templateUrl: './emoji.component.html',
  styleUrls: ['./emoji.component.css']
})
export class EmojiComponent  implements OnInit {

  @Output() emojiEmitter = new EventEmitter();
  @Input() emojis = [];
  constructor() {
  }

  ngOnInit() {

  }

  emit(emoji){
    this.emojiEmitter.emit(emoji);
  }

}
