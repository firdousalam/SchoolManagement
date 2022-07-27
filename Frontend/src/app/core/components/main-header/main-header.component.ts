import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/shared/services/api/common.service';

@Component({
  selector: 'app-main-header',
  templateUrl: './main-header.component.html',
  styleUrls: ['./main-header.component.scss'],
})
export class MainHeaderComponent implements OnInit {
  routeParamSubscription!:Subscription;
  studentProfileId:any;
  constructor(private commonService:CommonService,private router:Router,private route:ActivatedRoute) {}

  ngOnInit(): void {
    this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
     this.studentProfileId = 1;
    })
  }

  toggleMenu(){
    this.commonService.menuToggleSubject.next();
  }
}
