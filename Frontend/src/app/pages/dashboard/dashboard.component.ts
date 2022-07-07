import { Component, OnInit, HostBinding } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  @HostBinding('class') class = 'page-content-area';

  constructor() {}

  ngOnInit(): void {}

  notImplemented() {
    throw new Error('Not Implemented!');
  }
}
