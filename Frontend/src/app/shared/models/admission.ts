import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface IAdmission extends IAudit {
    applicationCategory: string;
    batchNo: string;
    certificateLink: string;
    courseCenter: string;
    dateofEntry: string;
    enrollmentNumber: string;
    id: number | null;
    idCardNo: string;
    profileId: number | null;
    validityTill: string;
}

export interface IAdmissionPage extends IPage {
    content:IAdmission[];
}

export const Admission = <IAdmission>{
    "applicationCategory": "",
    "batchNo": "",
    "certificateLink": "",
    "courseCenter": "",
    "createdAt": "",
    "createdBy": "",
    "dateofEntry": "",
    "enrollmentNumber": "",
    "id": 0,
    "idCardNo": "",
    "profileId": 0,
    "status": 0,
    "updatedAt": "",
    "updatedBy": "",
    "validityTill": ""
}

export interface IAdmissionSearch {
  referenceType?: string,
  status?: number | null,
  pageNumber?: number,
  pageSize?: number,
  id?:number|null,
}

export const AdmissionSearch = <IAdmissionSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: defaultPageSize,
  id: null,
};

export const activeAdmissionSearch = <IAdmissionSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: -1,
  id: null,
};
