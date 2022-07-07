import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { Toast } from 'bootstrap';
import { fromEvent } from 'rxjs';
import { take } from 'rxjs/operators';
import { NotificationTypes } from '../../../models/notification-types';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
})
export class NotificationComponent implements OnInit {
  @Output() disposeEvent = new EventEmitter();

  @ViewChild('notificationElement', { static: true })
  notificationElement!: ElementRef;

  @Input()
  type!: NotificationTypes;

  @Input()
  title!: string;

  @Input()
  message!: string;

  notification!: Toast;

  ngOnInit() {
    this.show();
  }

  show() {
    this.notification = new Toast(
      this.notificationElement.nativeElement,
      this.type === NotificationTypes.ERROR
        ? {
            autohide: false,
          }
        : {
            delay: 5000,
          }
    );

    fromEvent(this.notificationElement.nativeElement, 'hidden.bs.toast')
      .pipe(take(1))
      .subscribe(() => this.hide());

    this.notification.show();
  }

  hide() {
    this.notification.dispose();
    this.disposeEvent.emit();
  }
}
