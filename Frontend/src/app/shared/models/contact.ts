import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface IContact extends IAudit {
  id:number|null;
  referenceType: string;
  status: number | null;
  dateOfEntry: string, 
  queryOrClarification: string,
  queryStatus: string,
  response: string, 
  profileId:number|null;
  type: string
}

export interface IContactPage extends IPage {
    content:IContact[];
}

export const contact = <IContact>{
  createdAt: '',
  createdBy: '',
  referenceType: '',
  status:  null,
  dateOfEntry: '', 
  queryOrClarification: '',
  queryStatus: '',
  response: '', 
  type: '',
  updatedAt: '',
  updatedBy: '',
  id:null
}

export interface IContactSearch {
  referenceType?: string,
  status?: number | null,
  pageNumber?: number,
  sortDirection?:string,
  pageSize?: number,
  id?:number|null,
  profileId?:number|null;
}

export const contactSearch = <IContactSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: defaultPageSize,
  id: null,
};

export const activeContactSearch = <IContactSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: -1,
  id: null,
};
