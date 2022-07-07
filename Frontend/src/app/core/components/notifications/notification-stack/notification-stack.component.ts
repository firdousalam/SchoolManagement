import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import { INotificationEvent } from '../../../models/notification-event';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-notification-stack',
  templateUrl: './notification-stack.component.html',
  styleUrls: ['./notification-stack.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NotificationStackComponent implements OnInit {
  currentNotifications: INotificationEvent[] = [];

  constructor(
    private notificationService: NotificationService,
    private notificationChangeDetector: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.subscribeToNotifications();
  }

  subscribeToNotifications() {
    this.notificationService.notificationEvents.subscribe(
      (notifications: any) => {
        const currentNotification: INotificationEvent = {
          type: notifications.type,
          title: notifications.title,
          message: notifications.message,
        };
        this.currentNotifications.push(currentNotification);
        this.notificationChangeDetector.detectChanges();
      }
    );
  }

  dispose(index: number) {
    this.currentNotifications.splice(index, 1);
    this.notificationChangeDetector.detectChanges();
  }
}
