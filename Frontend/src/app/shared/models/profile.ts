import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface stdPersonalDetail {
    id: number | null;
    dateofEntry: string;
    applicantName: string;
    dob: string;
    gender: string;
    nationality: string;
    fatherOrGuardian: string;
    nameOfFatherOrGuardian: string;
    scOrSt: string;
    employmentStatus: string;
    stdContactDetail: number | null;
    profileId: number | null;
}
export interface stdEducationList {
    id: number | null;
    dateofEntry: string;
    sequence: number | null;
    courseName: string;
    institution: string;
    educationStatus: string;
    percentage: number|null;
    yearofCompletion: string;
    profileId: number | null;
}
export interface stdProfessionList {
    id: number | null;
    dateofEntry: string;
    organization: string;
    designation: string;
    placeOfDuty: string;
    profileId: number | null;
}
export interface stdAdmission {

    applicationCategory: string;
    batchNo: string;
    certificateLink: string;
    courseCenter: string;
    dateofEntry: string;
    enrollmentNumber: string;
    id:  number | null;
    idCardNo: string;
    profileId:  number | null;
    validityTill: string;

}


export interface IProfile extends IAudit {
    id: number | null;
    userId: number | null; 
    stdAdmission:stdAdmission[];
    stdProfessionList:stdProfessionList[];
    stdEducationList:stdEducationList[];
    stdPersonalDetail:stdPersonalDetail;
    
}

export interface IProfilePage extends IPage {
    content: IProfile[];
}

export const Profile = <IProfile>{
    id: null,
    userId: null, 
    stdPersonalDetail: {
        id: null,
        dateofEntry: '',
        applicantName: '',
        dob: '',
        gender: '',
        nationality: '',
        fatherOrGuardian: '',
        nameOfFatherOrGuardian: '',
        scOrSt: '',
        employmentStatus: '',
        stdContactDetail: null,
        profileId: null
    },
    stdEducationList: [],
    stdProfessionList: [],
    stdAdmission: [],
    createdAt: '',
    createdBy: '',
    updatedAt: '',
    updatedBy: '',
}

export interface IProfileSearch {
    referenceType?: string;
    status?: number | null;
    pageNumber?: number;
    pageSize?: number;
    profileId?: number | null;
}

export const ProfileSearch = <IProfileSearch>{
    referenceType: '',
    status: null,
    pageNumber: 0,
    pageSize: defaultPageSize,
    profileId: null
};

export const activeProfileSearch = <IProfileSearch>{
    referenceType: '',
    status: null,
    pageNumber: 0,
    pageSize: -1,
    profileId: null
};
