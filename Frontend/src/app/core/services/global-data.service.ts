import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GlobalDataService {
  public adminDetail = new BehaviorSubject<any>({});

  constructor() {}

  getAdminDetail(): Observable<any> {
    return this.adminDetail.asObservable();
  }
  updateAdminDetail(detail: any): void {
    this.adminDetail.next(detail);
  }
}
