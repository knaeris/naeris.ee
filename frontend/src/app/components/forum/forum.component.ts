import {Component, OnInit} from '@angular/core';
import {ThreadService} from "../../services/thread.service";
import {Thread} from "../../model/thread";

@Component({
  selector: 'app-forum',
  templateUrl: './forum.component.html',
  styleUrls: ['./forum.component.css']
})
export class ForumComponent implements OnInit {

  threads: Thread[];

  constructor(private threadService: ThreadService) { }

  ngOnInit() {
    this.getThreads();
  }

  private getThreads(){
    this.threadService.getAllThreads();
  }

}
