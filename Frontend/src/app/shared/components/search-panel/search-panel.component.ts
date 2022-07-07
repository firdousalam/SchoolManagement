import { Component, Output, EventEmitter, Inject } from '@angular/core';

@Component({
  template: '',
})
export abstract class SearchPanelComponent<ISearch> {
  @Output() panelClosed = new EventEmitter<boolean>();
  @Output() search = new EventEmitter<ISearch>();

  searchObject: ISearch;

  constructor(@Inject('searchInfo') protected searchInfo: ISearch) {
    this.searchObject = { ...searchInfo };
  }

  searchPanelClosed() {
    this.panelClosed.emit(true);
  }

  onSearch() {
    this.search.emit(this.searchObject);
  }
}
