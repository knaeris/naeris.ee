import {Component} from '@angular/core';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {WebsocketService} from "./services/websocket.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styles: []
})
export class AppComponent {
    title = 'frontend';
    jouki: string = "";

    constructor(private ws: WebsocketService) {
    }

}
