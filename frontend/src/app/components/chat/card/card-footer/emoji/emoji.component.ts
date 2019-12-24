import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
    selector: 'app-emoji',
    templateUrl: './emoji.component.html',
    styleUrls: ['./emoji.component.css']
})
export class EmojiComponent implements OnInit {

    @Output() emojiEmitter = new EventEmitter();
    @Input() emojis = [];

    constructor() {
    }

    ngOnInit() {

    }

    emit(emoji) {
        this.emojiEmitter.emit(emoji);
    }

}
