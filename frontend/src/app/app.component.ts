import {Component} from '@angular/core';
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
