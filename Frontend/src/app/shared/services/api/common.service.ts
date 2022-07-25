import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommonService {
 profileSubject: Subject<any> = new Subject<any>();
 menuToggleSubject: Subject<any> = new Subject<any>();
  constructor() { }
}
