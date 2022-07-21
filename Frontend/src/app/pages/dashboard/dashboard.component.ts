import { Component, OnInit, HostBinding, AfterViewInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/shared/services/api/notification.service';
import {INotification,INotificationPage} from 'src/app/shared/models/notification'
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
  constructor(private router:Router,private route:ActivatedRoute,private api: NotificationService) {}

  ngOnInit(): void {
    this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
      console.log(params);
      
      this.studentProfileId = params.get('id');
      this.Init();
    })
  }

  Init(){
    this.notificationSubscription = this.api.getAll().subscribe((data: INotification[]) => {
      console.log(data);
      
    });
  }
  notImplemented() {
    throw new Error('Not Implemented!');
  }
}
