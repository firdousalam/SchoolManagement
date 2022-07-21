import { Component, OnInit, HostBinding, OnDestroy } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import {
  studentProfileTabs,
  studentProfileTabNames,
} from 'src/app/shared/constants/admission.constant';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { IProfile, IProfilePage, stdAdmission, stdContactDetail, stdEducationList, stdPersonalDetail, stdProfession } from 'src/app/shared/models/profile'
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { NotificationService } from 'src/app/core/services/notification.service';
import { StudentPersonalServiceService } from 'src/app/shared/services/api/student-personal-service.service';
import { ProfileHeaderService } from 'src/app/shared/services/api/profile-header.service';
@Component({
  selector: 'app-student-profile-detail',
  templateUrl: './student-profile-detail.component.html',
  styleUrls: ['./student-profile-detail.component.scss'],
})
export class StudentProfileDetailComponent implements OnInit, OnDestroy {
  eventsSubject: Subject<void> = new Subject<void>();
  saveSubject: Subject<void> = new Subject<void>();

  @HostBinding('class') class = 'page-content-area';
  tabs: any;
  isValid: boolean = true
  selectedTab: string = '';
  employmentStatus: any = 'Employed';
  studentProfileTabNames: any = studentProfileTabNames;
  editMode = false;
  studentProfileData!: IProfile;
  profileSubscription: Subscription | undefined;
  routeParamSubscription: Subscription | undefined;
  submitStudentProfileSubscription: Subscription | undefined;
  savePersonalDataSubcription: Subscription | undefined;
  savePersonalheaderDataSubcription: Subscription | undefined;
  studentProfileId: any;
  studentEducationData: stdEducationList[] = [];
  studentPersonalData!: stdPersonalDetail;
  studentContactData!: stdContactDetail;
  studentAdmissionData!: stdAdmission;
  studentProfessionData!: stdProfession;
  subscriptionArray: any = [];
  constructor(private headerpersonalApi:ProfileHeaderService,private personalApi: StudentPersonalServiceService, private notificationService: NotificationService, private api: StudentProfileServiceService, private router: Router, private route: ActivatedRoute) {
    // this.tabs = studentProfileTabs;
    // this.selectedTab = studentProfileTabNames.PERSONAL
    this.routeParamSubscription = this.route.paramMap.subscribe((params: ParamMap) => {
      console.log(params);

      this.studentProfileId = params.get('id');
    })
    this.subscriptionArray.push(this.routeParamSubscription);
  }
  onEdit() {
    this.editMode = true;
  }
  onCancel() {
    this.editMode = false;
  }

  ngOnInit(): void {
    this.init();
  }

  submitAllData() {
    this.eventsSubject.next();
    console.log('Amirrrrrrrrrrrrr', this.studentEducationData);
    const request: IProfile = {
      "stdAdmission": { ...this.studentProfileData.stdAdmission, ...this.studentAdmissionData },
      "stdEducationList": [...this.studentProfileData.stdEducationList, ...this.studentEducationData],
      "stdPersonalDetail": { ...this.studentProfileData.stdPersonalDetail, ...this.studentPersonalData },
      "stdProfession": { ...this.studentProfileData.stdProfession, ...this.studentProfessionData },
      "stdContactDetail": { ...this.studentProfileData.stdContactDetail, ...this.studentContactData }
    };
    console.log('Submit Data:', request);
    this.studentProfileData = {
      ...this.studentProfileData,...request
    }
    this.submitStudentProfileSubscription = this.api.update(this.studentProfileData).subscribe((result: any) => {
      this.notificationService.showSuccessToast(result.message);
      this.init();
    })
  }

  updatedEducationData(data: stdEducationList[]) {
    this.studentEducationData = data;

  }
  updatedEmploymentData(data: stdProfession) {
    this.studentProfessionData = data;

  }
  savePersonal() {
    this.saveSubject.next();
    console.log('SSSS',this.employmentStatus);
    
    this.studentContactData = { ...this.studentContactData, 'profileId': parseInt(this.studentProfileId) };

    const requestData: any = { ...this.studentProfileData.stdContactDetail, ...this.studentContactData };
    delete requestData.status;
    delete requestData.approvalStatus;

    this.savePersonalheaderDataSubcription = this.headerpersonalApi.updateBySearchCriteria({profileId:this.studentProfileId},{...this.studentPersonalData,...{'employmentStatus':this.employmentStatus,'profileId': parseInt(this.studentProfileId)}}).subscribe((data:any)=>{
    })
    this.savePersonalDataSubcription = this.personalApi.updateBySearchCriteria({ profileId: this.studentProfileId }, requestData).subscribe((data: any) => {
      this.notificationService.showSuccessToast(data.message);
      this.editMode = false;
      this.init();
    })
    this.subscriptionArray.push(this.savePersonalDataSubcription);
  }
  isFormValid(valid: boolean): void {
    this.isValid = valid
  }
  updatedPersonalData(data: stdContactDetail | any) {
    this.studentContactData = data;

  }

  getStatus(status:any){
    this.employmentStatus = status;
  }
  // onTabSelect(tabName: any) {
  //   this.selectedTab = tabName;
  // }

 
  init() {
    const request = { profileId: this.studentProfileId }
    this.profileSubscription = this.api.getByIDWithDetail(request).subscribe((data: IProfile) => {
      console.log(data);
      this.studentProfileData = data;
      this.employmentStatus = this.studentProfileData.stdPersonalDetail.employmentStatus;
      this.studentEducationData = this.studentProfileData.stdEducationList;
      this.studentProfessionData = this.studentProfileData.stdProfession;
      this.studentPersonalData = this.studentProfileData.stdPersonalDetail;
      this.studentAdmissionData = this.studentProfileData.stdAdmission;
      this.studentContactData = this.studentProfileData.stdContactDetail;
    })
    this.subscriptionArray.push(this.profileSubscription);
  }

  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
 
}
