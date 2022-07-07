import { Component } from '@angular/core';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.scss'],
})
export class SideNavComponent {

  constructor() {}
  isCollapsed: boolean = false;

  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
