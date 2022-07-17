import { ColDef } from "ag-grid-community";
import { ITableViewConfig } from "../models/table-view";

export const admissionPaymentTabNames ={
    feePayment : 'Fee Payment',
    paidPayment : 'View Fees Paid'
};

export const admissionPaymentTabs = [
    'Fee Payment','View Fees Paid'
]
export enum admissionTabNames {
    ID_CARD = 'ID Card',
    APPLICANT_LIST = 'Applicant List',
    STUDENT_PROFILE = 'Student Profile',
  }
  
  export const admissionTabs = [
    admissionTabNames.ID_CARD,
    admissionTabNames.APPLICANT_LIST,
    admissionTabNames.STUDENT_PROFILE,
  ];
  
  export enum studentProfileTabNames {
    PERSONAL = 'Personal',
    EDUCATIONAL = 'Educational',
    EMPLOYMENT = 'Employment',
    ADMISSION_DETAILS = 'Admission Details',
  }
  
  export const studentProfileTabs = [
    studentProfileTabNames.PERSONAL,
    studentProfileTabNames.EDUCATIONAL,
    studentProfileTabNames.EMPLOYMENT,
    studentProfileTabNames.ADMISSION_DETAILS,
  ];
  
  export const profileColumnDefs: ITableViewConfig[] = [
    { header: 'Applicant Name', field: 'applicantName' },
    { header: 'Enrollment No', field: 'enrollmentNumber' },
    { header: 'Batch', field: 'batchNo' },
    { header: 'Category', field: 'applicationCategory' },
    { header: 'ID Card No', field: 'idCardNo' },
    { header: 'Validity Till', field: 'validityTill' },
  ];
  
  export const educationDetailsColumnDefs: ColDef[] = [
    { headerName: 'SI No', field: 'id' },
    { headerName: 'Course Name', field: 'courseName' },
    { headerName: 'Institution', field: 'institution' },
    { headerName: 'Status', field: 'educationStatus' },
    { headerName: 'Percentage', field: 'percentage' },
    { headerName: 'Year of Passing', field: 'yearofCompletion' },
  ];
  
  export const applicantListColumnDefs: ITableViewConfig[] = [
    { header: 'Applciaiton No', field: 'enrollmentNumber' },
    { header: 'Date', field: 'dateofEntry' },
    { header: 'Applicant Name', field: 'applicantName' },
    { header: 'Date of Birth', field: 'dateOfBirth' },
    { header: 'Father/Guardian', field: 'fatherOrGuardian' },
    { header: 'SC/ST', field: 'scSt' },
    { header: 'Nationality', field: 'nationality' },
    { header: 'Status', field: 'status' },
  ];
  
  export const idCardColumnDefs: ColDef[] = [
    { headerName: 'SI.No', field: 'siNo' },
    { headerName: 'Applicant Name', field: 'applicantName' },
    { headerName: 'Enrollment No', field: 'enrollmentNo' },
    { headerName: 'Batch No', field: 'batchNo' },
    { headerName: 'Valid Upto', field: 'validUpto' },
    { headerName: 'Address', field: 'address' },
    { headerName: 'Phone No', field: 'phoneNo' },
    { headerName: 'Photo Scanned', field: 'photoScanned' },
  ];
  