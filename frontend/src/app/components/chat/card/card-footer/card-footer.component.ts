import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ChatService} from "../../../../services/chat.service";
import {Message} from "../../../../model/message";
import {Observable} from "rxjs";
import {WebsocketService} from "../../../../services/websocket.service";
import {CardComponent} from "../card.component";

@Component({
    selector: 'app-card-footer',
    templateUrl: './card-footer.component.html',
    styleUrls: ['./card-footer.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class CardFooterComponent extends CardComponent implements OnInit {
    content: string = "";

    constructor(webSocketService: WebsocketService,
                chatService: ChatService) {
        super(webSocketService, chatService);
    }

    ngOnInit() {
    }

    sendMessage() {
        this.content.trim();
        if (this.content != "") {
            super.sendMessage(this.content)
            this.content = "";
        }
    }


    getEmojis() {
        return [0x1F600, 0x1F604, 0x1F34A, 0x1F344, 0x1F37F, 0x1F363, 0x1F370, 0x1F355,
            0x1F354, 0x1F35F, 0x1F6C0, 0x1F48E, 0x1F5FA, 0x23F0, 0x1F579, 0x1F4DA,
            0x1F431, 0x1F42A, 0x1F439, 0x1F424].map(item => String.fromCodePoint(item));
    }

    appendSmile(item) {
        return this.content += item;
    }

    getSmileEmoji() {
        return String.fromCodePoint(0x1F407);
    }


}
