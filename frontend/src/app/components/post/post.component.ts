import {Component, Input, OnInit} from '@angular/core';
import {ThreadService} from "../../services/thread.service";
import {Thread} from "../../model/thread";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  @Input('thread') thread: Thread;

  constructor(private srv: ThreadService) { }

  ngOnInit() {
  }

}
