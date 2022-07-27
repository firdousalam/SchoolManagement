import { Component, OnInit, HostBinding, AfterViewInit, Output } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/shared/services/api/notification.service';
import { INotification, INotificationPage } from 'src/app/shared/models/notification'
import { CommonService } from 'src/app/shared/services/api/common.service';
import { isEmpty } from 'lodash';
import { IProfileSearch } from 'src/app/shared/models/profile';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
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
  constructor(private commonService: CommonService, private router: Router, private route: ActivatedRoute, private api: NotificationService,
    private profileService: StudentProfileServiceService) {

    /*
    const profileObjLS:any = localStorage.getItem('studentProfileId');
    if(isEmpty(profileObjLS)){
      const  profileObj:any={studentProfileId:4,userId:4};
      localStorage.setItem('studentProfileId',JSON.stringify(profileObj));
    }
    */
    //const userId = this.route.snapshot.paramMap.get('id')?.toString(); 
    const profileObjLS: any = localStorage.getItem('studentProfileId');
    console.log(profileObjLS,"pfffff")
    if (isEmpty(profileObjLS)) {
      const profileObj: any = { studentProfileId: 4, userId: 4 };
      localStorage.setItem('studentProfileId', JSON.stringify(profileObj));
    }
  }

  async ngOnInit(): Promise<any> {
    // this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
    // console.log(params);

    let profileObjLS: any = JSON.parse(localStorage.getItem('studentProfileId')!);
    console.log(typeof profileObjLS);
    let userId = profileObjLS?.userId;
    console.log(profileObjLS["userId"],"46");
    //const userId = '1'; 
    const params: IProfileSearch = { userId: parseInt(userId) };
    try {
      let studentProfile:any = await this.profileService.getProfileByUserID(params).toPromise();
      console.log(studentProfile);
      const profileObj: any = { studentProfileId: studentProfile.messageCode, userId: userId };
      localStorage.setItem('studentProfileId', JSON.stringify(profileObj));
      this.studentProfileId = studentProfile.studentProfileId;
      this.commonService.profileSubject.next({ profileId: this.studentProfileId });
      localStorage.setItem('profileExist', "yes");
      this.profileExist= "yes";
      this.Init();

    } catch (error) {
     
      localStorage.setItem('profileExist', "no");
      this.profileExist= "no";
      // status set to show only application menu
    };
    
  }

  Init() {
    this.notificationSubscription = this.api.getAll().subscribe((data: INotification[]) => {
      console.log(data);

    });
  }
  notImplemented() {
    throw new Error('Not Implemented!');
  }
  openApplication(): void {
    this.router.navigate(['/application']);
  }
}
