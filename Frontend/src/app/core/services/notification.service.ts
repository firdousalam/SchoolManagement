import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { NotificationTypes } from '../models/notification-types';
import { INotificationEvent } from '../models/notification-event';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  notificationEvents: Observable<INotificationEvent>;
  private _notificationEvents = new Subject<INotificationEvent>();

  constructor() {
    this.notificationEvents = this._notificationEvents.asObservable();
  }

  showSuccessToast(message: string, title: string = 'Success') {
    this._notificationEvents.next({
      message,
      title,
      type: NotificationTypes.SUCCESS,
    });
  }

  showInfoToast(message: string, title: string = 'Info') {
    this._notificationEvents.next({
      message,
      title,
      type: NotificationTypes.INFO,
    });
  }

  showWarningToast(message: string, title: string = 'Warning') {
    this._notificationEvents.next({
      message,
      title,
      type: NotificationTypes.WARNING,
    });
  }

  showErrorToast(message: string, title: string = 'Error') {
    this._notificationEvents.next({
      message,
      title,
      type: NotificationTypes.ERROR,
    });
  }
}
