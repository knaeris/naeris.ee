import {AfterViewChecked, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ChatSession} from "../../../model/chatsession";
import {ChatComponent} from "../chat.component";
import {WebsocketService} from "../../../services/websocket.service";
import {ChatService} from "../../../services/chat.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-card',
    templateUrl: './card.component.html',
    styleUrls: ['./card.component.css']
})
export class CardComponent extends ChatComponent implements OnInit, AfterViewChecked {
    @Input() chat: ChatSession;
    @ViewChild('scrollDown') private scrollContainer: ElementRef;

    constructor(webSocketService: WebsocketService,
                chatService: ChatService,
                router: Router) {
        super(webSocketService, chatService, router);
    }

    ngOnInit() {
        this.scrollToBottom();
    }

    ngAfterViewChecked() {
        this.scrollToBottom();
    }

    scrollToBottom(): void {
        try {
            this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
        } catch (err) {
        }
    }

}
