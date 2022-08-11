import { IAudit } from './audit';
import { defaultPageSize } from '../constants/common.constant';
import { IPage } from './page';

export interface stdContactDetail {
    alternateMobileNumber: string;
    caddress: string;
    cdistrict: string;
    cpinCode: string;
    cstate: string;
    dateofEntry ?: string;
    emailId: string;
    isPASameAsCA: string;
    landlineNumber: string;
    mobileNumber: string;
    paddress: string;
    pdistrict: string;
    personalId: number | null
    ppinCode: string;
    pstate: string;
    signature:string;
    profileId?: number | null;
}
export interface stdPersonalDetail {
    id: number | null;
    dateofEntry ?: string | null;
    applicantName: string;
    dob: string;
    gender: string;
    nationality: string;
    fatherOrGuardian: string;
    pictureLink: string;
    nameOfFatherOrGuardian: string;
    scOrSt: string;
    employmentStatus: string;
    profileId?: number | null;
    docTempList?: [number];
}
export interface stdEducationList {
    id: number;
    dateofEntry ?: string;
    sequence?: number | null;
    courseName: string;
    institution: string;
    educationStatus: string;
    percentage: string;
    yearofCompletion: string;
    profileId?: number | null;
}
export interface stdProfession {
    id: number | null;
    dateofEntry ?: string;
    organization: string;
    designation: string;
    placeOfDuty: string;
    profileId?: number | null;
}
export interface stdAdmission {

    applicationCategory: string;
    batchNo: string;
    certificateLink: string;
    courseCenter: string;
    dateofEntry ?: string;
    enrollmentNumber: string;
    id:  number | null;
    idCardNo: string;
    profileId?:  number | null;
    validityTill: string;

}


export interface IProfile extends IAudit {
    id?: number | null;
    userId?: number | null; 
    stdAdmission:stdAdmission;
    stdProfession:stdProfession;
    stdEducationList:stdEducationList[];
    stdPersonalDetail:stdPersonalDetail;
    stdContactDetail: stdContactDetail;
    docTempList?:number[];
    
}

export interface IProfilePage extends IPage {
    stdAdmission: any;
    docList: any;
    stdEducationList: any;
    stdProfession: any;
    stdContactDetail: any;
    stdPersonalDetail: any;
    content: IProfile[];
}

export const Profile = <IProfile><unknown>{
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
        stdContactDetail: {
            alternateMobileNumber: '',
            caddress: '',
            cdistrict: '',
            cpinCode: '',
            cstate: '',
            dateofEntry: '',
            emailId: '',
            isPASameAsCA: '',
            landlineNumber: '',
            mobileNumber: '',
            paddress: '',
            pdistrict: '',
            personalId: null,
            ppinCode: '',
            pstate: '',
            signature:''
        },
        profileId: null
    },
    stdEducationList: [],
    stdProfession: {
        id: null,
        dateofEntry: '',
        organization: '',
        designation: '',
        placeOfDuty: '',
        profileId: null
    },
    stdAdmission: []
}

export interface IProfileSearch {
    referenceType?: string;
    status?: number | null;
    userId?: number | null;
    pageNumber?: number;
    pageSize?: number;
    profileId?: number | null;
    docCategory?:string;
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
