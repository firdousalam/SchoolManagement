import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface IType extends IAudit {
  description: string;
  id: number | null;
  name: string;
  referenceType: string;
  status: number | null;
}

export interface ITypePage extends IPage {
  content: IType[];
}

export const type = <IType>{
  createdAt: '',
  createdBy: '',
  description: '',
  id: null,
  referenceType: '',
  status: null,
  updatedAt: '',
  updatedBy: '',
};

export interface ITypeSearch {
  referenceType?: string;
  status?: number | null;
  pageNumber?: number;
  pageSize?: number;
}

export const typeSearch = <ITypeSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: defaultPageSize,
};

export const activeTypeSearch = <ITypeSearch>{
  referenceType: '',
  status: null,
  pageNumber: 0,
  pageSize: -1,
};
