import { Component, OnDestroy } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/shared/services/api/common.service';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.scss'],
})
export class SideNavComponent implements OnDestroy {
  routeParamSubscription!:Subscription;
  profileSubscription!:Subscription;
  subArray:any=[];
  studentProfileId:any;
  constructor(private router:Router,private route:ActivatedRoute,private commonService:CommonService) {}
  isCollapsed: boolean = false;

  ngOnInit(): void {
    

    this.profileSubscription = this.commonService.profileSubject.subscribe((x:any)=>{
      console.log(x);
      this.studentProfileId = x.profileId;
      
    })
    this.subArray.push(this.profileSubscription);
  }
  ngOnDestroy(): void {
    this.subArray.array.forEach((element:any) => element.unsubscribe());
  }
  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
