import { Component, OnInit, HostBinding, AfterViewInit, Output } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/shared/services/api/notification.service';
import {INotification,INotificationPage} from 'src/app/shared/models/notification'
import { CommonService } from 'src/app/shared/services/api/common.service';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  @HostBinding('class') class = 'page-content-area';
  routeParamSubscription!:Subscription;
  studentProfileId:any;
  notificationSubscription!: Subscription;
  constructor(private commonService:CommonService,private router:Router,private route:ActivatedRoute,private api: NotificationService) {
    const profileObj:any={studentProfileId:1,userId:1};
    localStorage.setItem('studentProfileId',JSON.stringify(profileObj));
  }

  ngOnInit(): void {
   // this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
     // console.log(params);
     const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
     this.studentProfileId = studentProfileObj.studentProfileId;
      this.commonService.profileSubject.next({profileId:this.studentProfileId});
      this.Init();
   // })
  }

  Init(){
    this.notificationSubscription = this.api.getAll().subscribe((data: INotification[]) => {
      console.log(data);
      
    });
  }
  notImplemented() {
    throw new Error('Not Implemented!');
  }
  openApplication():void{
    this.router.navigate(['/application']);
  }
}
