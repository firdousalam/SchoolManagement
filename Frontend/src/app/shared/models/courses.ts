import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface Lang {
    docName: string;
    id: number | null
}
export interface ICourse extends IAudit {
      no:number | null;
      batchId: number | null;
      batchName: string;
      description: string;
      engdata: Lang[];
      fromDate: string;
      heading: string;
      id: number | null;
      malData: Lang[];
      status: number | null;
      subject: number | null;
      toDate: string;
}

export interface ICoursePage extends IPage {
    content:ICourse[];
}

 

export interface ICourseSearch {
  referenceType?: string,
  status?: number | null,
  pageNumber?: number,
  pageSize?: number,
  id?:number|null,
}

export const contactSearch = <ICourseSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: defaultPageSize,
  id: null,
};

export const activeContactSearch = <ICourseSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: -1,
  id: null,
};
