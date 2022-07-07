import { Component, OnInit, Input, HostBinding } from '@angular/core';

@Component({
  selector: 'app-heading',
  templateUrl: './heading.component.html',
  styleUrls: ['./heading.component.scss'],
})
export class HeadingComponent implements OnInit {
  @HostBinding('class') class = 'heading';
  @Input() heading = '';
  @Input() secondary: boolean = false;

  constructor() {}

  ngOnInit(): void {}
}
