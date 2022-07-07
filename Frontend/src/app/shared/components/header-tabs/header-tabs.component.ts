import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-header-tabs',
  templateUrl: './header-tabs.component.html',
  styleUrls: ['./header-tabs.component.scss'],
})
export class HeaderTabsComponent implements OnInit {
  @Input() tabs = [];
  @Input() selectedTab = '';
  @Output() tabChange = new EventEmitter<string>();

  constructor() {}

  ngOnInit(): void {}

  onTabSelect(tab: any) {
    this.selectedTab = tab;
    this.tabChange.emit(tab);
  }
}
