import { NotificationTypes } from './notification-types';

export interface INotificationEvent {
  type: NotificationTypes;
  title: string;
  message: string;
}
