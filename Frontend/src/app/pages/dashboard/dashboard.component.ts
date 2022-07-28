import { Component, OnInit, HostBinding, AfterViewInit, Output } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/shared/services/api/notification.service';
import { INotification, INotificationPage } from 'src/app/shared/models/notification'
import { CommonService } from 'src/app/shared/services/api/common.service';
import { isEmpty } from 'lodash';
import { IProfileSearch } from 'src/app/shared/models/profile';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  @HostBinding('class') class = 'page-content-area';
  routeParamSubscription!: Subscription;
  studentProfileId: any;
  notificationSubscription!: Subscription;
  profileExist = "";
  notificationList:INotification[]=[];
  constructor(private datePipe:DatePipe,private commonService: CommonService, private router: Router, private route: ActivatedRoute, private api: NotificationService,
    private profileService: StudentProfileServiceService) {
    const profileObjLS: any = localStorage.getItem('studentProfileId');

    if (isEmpty(profileObjLS)) {
      const profileObj: any = { studentProfileId: 4, userId: 4 };
      localStorage.setItem('studentProfileId', JSON.stringify(profileObj));
    }
  }

  async ngOnInit(): Promise<any> {
  
    let profileObjLS: any = JSON.parse(localStorage.getItem('studentProfileId')!);
   
    let userId = profileObjLS?.userId;

    const params: IProfileSearch = { userId: parseInt(userId) };
    try {
      //getStdProfileListByUserId
      let studentProfile:any = await this.profileService.getProfileByUserID(params).toPromise();

      const profileObj: any = { studentProfileId: studentProfile.id, userId: userId };
      localStorage.setItem('studentProfileId', JSON.stringify(profileObj));
      this.studentProfileId = studentProfile.studentProfileId;
      this.commonService.profileSubject.next({ profileId: this.studentProfileId });
      
      console.log(studentProfile);
      if(studentProfile.application.approvalStatus === 'Approved'){
        localStorage.setItem('profileExist', "yes");
        this.profileExist= "yes";
      }
      else
      {
        localStorage.setItem('profileExist', "no");
        this.profileExist= "no";
      }
      
      

    } catch (error) {
 
      localStorage.setItem('profileExist', "no");
      this.profileExist= "no";
   
    };
    this.commonService.menushowhideSubject.next(this.profileExist);
    this.Init();
  }

  Init() {
    this.notificationSubscription = this.api.getAll().subscribe((data: INotification[]) => {
      this.notificationList = data;
    });
  }
  notImplemented() {
    throw new Error('Not Implemented!');
  }
  openApplication(): void {
    this.router.navigate(['/application']);
  }

  getNotiDate(date?:string):any{
  return (date && moment(date,'DD/MM/YYYY', true).isValid() ? date : this.datePipe.transform(date,'dd/MM/yyyy'))
  }
}
