import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class SpinnerService {
  spinnerStatus$: Observable<boolean>;

  private spinnerSource = new BehaviorSubject<boolean>(false);
  private spinnerMap = new Map<string, boolean>();

  constructor() {
    this.spinnerStatus$ = this.spinnerSource.asObservable().pipe(delay(0));
  }

  setSpinner(spinner: boolean, url: string): void {
    if (!url) {
      throw new Error(
        'The request URL must be provided to the SpinnerService.setLoading function'
      );
    }
    if (spinner) {
      this.spinnerMap.set(url, spinner);
      this.spinnerSource.next(true);
    }
    if (!spinner && this.spinnerMap.has(url)) {
      this.spinnerMap.delete(url);
    }
    if (this.spinnerMap.size === 0) {
      this.spinnerSource.next(false);
    }
  }
}
