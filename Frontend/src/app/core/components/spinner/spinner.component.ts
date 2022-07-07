import { Component, OnInit, HostBinding } from '@angular/core';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss'],
})
export class SpinnerComponent implements OnInit {
  @HostBinding('class') class = 'spinner';

  constructor() {}

  ngOnInit(): void {}
}
