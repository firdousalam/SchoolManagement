import { Component, OnInit } from '@angular/core';
import { applicationObjectCreation } from 'src/app/shared/adaptor/adaptor';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { StudentDocumentService } from 'src/app/shared/services/api/student-document.service';
import { DataService } from 'src/app/shared/services/api/data.service';
import { ITypeSearch } from 'src/app/shared/models/type';
import { IProfileSearch, Profile } from 'src/app/shared/models/profile';
import { setSession, getSession } from 'src/app/shared/common/storage';
import { isEmpty, toNumber } from 'lodash';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
import { Content } from '@angular/compiler/src/render3/r3_ast';
import { PaymentModalComponent } from '../payment-modal/payment-modal.component';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NotificationService } from 'src/app/core/services/notification.service';



@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss']
})
export class ApplicationComponent implements OnInit {
  profileSubscription!: Subscription;
  applicationStatus: string = '';
  secondary: boolean = false;
  heading: string = 'Dashboard';
  applicationSubmitted: boolean = false;
  saveAsDraft: boolean = false;
  model: any;
  userId: any;
  referenceDate: any = moment().format('DD/MM/YYYY');
  studentApplication: any = {
    personalDetails: {},
    stdProfessionList: [],
    stdEducationList: [],
    stdContactDetail: {},
    docList: [],
    docTempList: []
  }
  studentApplicationDocDetail: any = [];
  educationRecord: any = {};
  //applicationViewDetails: IProfile = Profile;
  applicationViewDetails: any;
  professionalDetailsShow: boolean = false;
  personal: any;
  application: any;
  contact: any;
  profession: any;
  education: any;
  admission: any;
  docList: any;
  editDraft: any;
  pageTraverseStatus: string = 'APPLICATION_STARTED'
  documentTypeList: any;
  paymentApi: any;
  documentRecord: any;
  constructor(private profileService: StudentProfileServiceService,
    private router: Router,
    private route: ActivatedRoute,
    private date: DatePipe,
    private dataService: DataService,
    private modal: NzModalService,
    private studentDocument: StudentDocumentService,
    private noti: NotificationService) { }

  async ngOnInit(): Promise<any> {
    //const userId = this.route.snapshot.paramMap.get('id')?.toString(); 
    const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
    this.userId = studentProfileObj.userId;
    //const userId = '1'; 
    const params: IProfileSearch = { userId: toNumber(this.userId) };
    try {
      let studentProfile = await this.profileService.getProfileByUserID(params).toPromise();
      this.pageTraverseStatus = this.pageCheck(studentProfile, 'FORMTAB');
      this.applicationStatus = (this.pageTraverseStatus === 'APPLICATION_STARTED') ? '1' : '4';
      this.educationRecord = {
        courseName: "",
        institution: "",
        percentage: "",
        yearofCompletion: "",
        educationStatus: "",
        sequence: 0
      };
      this.studentApplication.stdEducationList.push(this.educationRecord);
      this.loadDocumentTypeList();
    } catch (error) {
      let studentProfile = "";
      this.pageTraverseStatus = this.pageCheck(studentProfile, 'FORMTAB');
      this.applicationStatus = (this.pageTraverseStatus === 'APPLICATION_STARTED') ? '1' : '4';
      this.educationRecord = {
        courseName: "",
        institution: "",
        percentage: "",
        yearofCompletion: "",
        educationStatus: "",
        sequence: 0
      };
      this.studentApplication.stdEducationList.push(this.educationRecord);
      this.loadDocumentTypeList();
    }
    this.documentRecord = {
      id: "",
      description: "",
      file: ""
    };
    this.studentApplicationDocDetail.push(this.documentRecord);
  }
  //Add more in education section
  addRow() {
    this.educationRecord = {
      courseName: "",
      institution: "",
      percentage: "",
      yearofCompletion: "",
      educationStatus: "",
      sequence: 0
    };

    // this.studentApplication.stdEducationList.push(this.educationRecord);
    this.studentApplication.stdEducationList = [...this.studentApplication.stdEducationList, this.educationRecord]
    return true;
  }
  //Add more in document section
  addDocumentRow() {
    this.documentRecord = {
      id: "",
      description: "",
      file: ""
    };
    this.studentApplicationDocDetail = [...this.studentApplicationDocDetail, this.documentRecord];
  
  }
  deleteRow(index: number) {
    this.studentApplication.stdEducationList.splice(index, 1);
  }
  deleteRowEdit(index: number) {
    this.editDraft.stdEducationList.splice(index, 1);
  }
  async applicationStatusChange(status: string, tab: string): Promise<any> {
    //const userId = this.route.snapshot.paramMap.get('id')?.toString();
    const userId = this.userId;
    const params: IProfileSearch = { userId: toNumber(userId) };
    try {
      let studentProfile: any = await this.profileService.getProfileByUserID(params).toPromise();
      if (status === "applicationForm") {
        try {
          if (!isEmpty(studentProfile)) {
            this.pageTraverseStatus = this.pageCheck(studentProfile, tab);
            if (this.pageTraverseStatus === 'AWAITING_APPROVAL') {
              this.applicationStatus = '4';
            } else if (this.pageTraverseStatus === 'DRAFT') {
              this.applicationStatus = '7';
            }
          } else {
            this.applicationStatus = '1';
          }
        }
        catch (error) {
          console.log(error);
        }
      } else if (status === "saveAsDraft") {
        if (!isEmpty(studentProfile)) {
          this.pageTraverseStatus = this.pageCheck(studentProfile, tab);
          if (studentProfile.application.applicationState === 'Draft') {
            this.applicationStatus = "3";
            this.saveAsDraft = true;
            this.editDraft = studentProfile;
          } else {
            this.pageTraverseStatus = 'DRAFTSUBMITTED'
            this.applicationStatus = "7";
          }
        } else {
          this.pageTraverseStatus = 'NODRAFT'
          this.applicationStatus = "6";
        }
      }
    } catch (error) {
      console.log(error);
    }
  }
  //APPLICATION SUBMITTED
  applicationSubmit(requestData: any) {
    //this.userId = this.route.snapshot.paramMap.get('id')?.toString();
    try {
      this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId, 'Awaiting Approval')).subscribe((data: any) => {
        this.noti.showSuccessToast('Student Profile Successfully Created')
        const profileObj: any = { studentProfileId: data.messageCode, userId: this.userId };
        localStorage.setItem('studentProfileId', JSON.stringify(profileObj));
        localStorage.setItem('profileExist', "no");
        //Modal need to be added here Amir
        this.showPopUp();
        //setSession('profileId', data.messageCode);
        //setSession('userId', this.userId)
        this.applicationStatus = '4';
      }, (error: Error) => {
        console.log(error);
        this.noti.showErrorToast('Student Profile Not Created, Contact Admin!')

      })
    } catch (error) {
      this.noti.showErrorToast('Student Profile Not Created, Contact Admin!')
    }
  }
  //APPLICATION SUBMITTED POST DRAFT
  applicationSubmittedPostDraft(requestData: any) {
    //this.userId = this.route.snapshot.paramMap.get('id')?.toString();

    try {
      this.profileSubscription = this.profileService.update(applicationObjectCreation(requestData, this.userId, 'Awaiting Approval')).subscribe((data: any) => {
        //Modal need to be added here Amir
        this.showPopUp();
        setSession('profileId', data.messageCode);
        setSession('userId', this.userId)
        this.applicationStatus = '4';
      }, (error: Error) => {
        console.log(error);
      })
    } catch (error) {
      this.noti.showErrorToast('Student Profile Not Created, Contact Admin!')
    }
  }
  async applicationView(tab: string): Promise<any> {
    let userId = this.userId;
    if (this.userId) {
      const params: IProfileSearch = { userId: toNumber(userId) };
      try {
        const studentProfile = await this.profileService.getProfileByUserID(params).toPromise();
        //user profile created      
        if (!isEmpty(studentProfile)) {
          this.pageTraverseStatus = this.pageCheck(studentProfile, tab);
          this.applicationStatus = (this.pageTraverseStatus === 'AWAITING_APPROVAL') ? '2' : '5';
          if (this.applicationStatus === '2') {
            // if(this.pageTraverseStatus === 'DRAFT' || this.pageTraverseStatus === 'APPLICATION_STARTED'){
            //   this.applicationStatus = "5";
            // }else{
            this.application = studentProfile;
            this.personal = studentProfile?.stdPersonalDetail;
            this.contact = studentProfile?.stdContactDetail;
            this.profession = studentProfile?.stdProfession;
            this.education = studentProfile?.stdEducationList;
            this.docList = studentProfile?.docList;
            this.admission = studentProfile?.stdAdmission;
          }
          //}    
        }
      } catch (error) {
        console.log(error);
      }
    } else {
      this.applicationStatus = "5";
    }
  }

  //APPLICATION SUBMITTED AS A SAVE AS DRAFT
  applicationDraft(requestData: any) {
    //this.userId = this.route.snapshot.paramMap.get('id')?.toString();    
    //this.userId = '1';   
    try {
      this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId, 'Saved')).subscribe((data: any) => {
        setSession('profileId', data.messageCode);
        setSession('userId', this.userId)
        this.applicationStatus = '3';
        this.saveAsDraft = true;
      }, (error: Error) => {
        console.log(error);
      })
    } catch (error) {
      this.noti.showErrorToast('Student Profile Not Created, Contact Admin!')
    }
  }

  sameCorrespondingAddress(check: string) {
    if (check === 'YES') {
      this.studentApplication.stdContactDetail.paddress = this.studentApplication.stdContactDetail.caddress
      this.studentApplication.stdContactDetail.pdistrict = this.studentApplication.stdContactDetail.cdistrict;
      this.studentApplication.stdContactDetail.pstate = this.studentApplication.stdContactDetail.cstate;
      this.studentApplication.stdContactDetail.ppinCode = this.studentApplication.stdContactDetail.cpinCode;
    } else {
      this.studentApplication.stdContactDetail.paddress = null;
      this.studentApplication.stdContactDetail.pdistrict = null;
      this.studentApplication.stdContactDetail.pstate = null;
      this.studentApplication.stdContactDetail.ppinCode = null;
    }
  }

  userCheck(page1: any, page2: any): any {
    if (!isEmpty(getSession('userId'))) {
      if (!isEmpty(getSession('profileId'))) {
        //this.userId = this.route.snapshot.paramMap.get('id')?.toString();
        if (this.userId !== getSession('userId')) {
          return page1;
        } else {
          return page2;
        }
      } else {
        return page1;
      }
    } else {
      return page1;
    }
  }

  documntUpload(event: any, document: any): any {
    const formData = new FormData();
  
    formData.append("file", event.target.files[0]);
    formData.append("id", document.id);
    formData.append("description", document.description);
    formData.append("documentType", 'Application Documents');
    this.studentDocument.fileUpload(formData).subscribe((x: any) => {
      if (x.status === 201) {
        this.studentApplication.docList.push(x.body);
        this.studentApplication.docTempList.push(x.body);
        this.noti.showSuccessToast('File uploaded successfully')
      }
    });


    //this.file = event.target.files[0];

    //this.file = event.target.files[0];

  }
  pageCheck(studentProfile: any, tab: string): string {
    if (!isEmpty(studentProfile)) {
      if (tab === 'VIEWTAB') {
        return 'AWAITING_APPROVAL';
      } else if (tab === 'DRAFTVIEWTAB') {
        return 'DRAFT';
      }
      else {
        return 'AWAITING_APPROVAL';
      }
    } else {
      //When there is no user id or profile id created
      return 'APPLICATION_STARTED';
    }
  }

  async loadDocumentTypeList(): Promise<any> {
    const referenceType: ITypeSearch = { referenceType: 'Application Documents', pageSize: 20 };
    const documentTypeList = await this.dataService.getBySearchCriteria(referenceType).toPromise();
    if (!isEmpty(documentTypeList?.content)) {
      this.documentTypeList = documentTypeList.content;
    }
  }

  professionStatus(param: string): void {
    this.professionalDetailsShow = (param === 'EMPLOYEE') ? false : true;
  }
  //Modal to be open for Payment
  showPopUp() {
    this.modal.create({
      nzTitle: '',
      nzContent: PaymentModalComponent,
      nzFooter: null,
      nzClosable: false,
      nzMaskClosable: false,
    })
  }
}
