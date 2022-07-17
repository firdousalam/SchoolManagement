import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface INotification extends IAudit {
    attachmentIds: number[] | null;
    batchId: number | null;
    content: string;
    dateofEntry: string;
    docIds: number[] | null;
    id: number | null;
    notificationType: string;
    sendTo: string;
    sendToInstitutionId: number | null;
    sendToStudentId: number | null;
}

export interface INotificationPage extends IPage {
  content: INotification[];
}

export const Notification = <INotification>{
    attachmentIds: null,
    batchId: null,
    content: '',
    dateofEntry: '',
    docIds: null,
    id: null,
    notificationType: '',
    sendTo: '',
    sendToInstitutionId: null,
    sendToStudentId: null
};

export interface INotificationSearch {
  referenceNotification?: string;
  status?: number | null;
  pageNumber?: number;
  pageSize?: number;
}

export const NotificationSearch = <INotificationSearch>{
  referenceNotification: '',
  status: null,
  pageNumber: 0,
  pageSize: defaultPageSize,
};

export const activeNotificationSearch = <INotificationSearch>{
  referenceNotification: '',
  status: null,
  pageNumber: 0,
  pageSize: -1,
};
