import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-main-header',
  templateUrl: './main-header.component.html',
  styleUrls: ['./main-header.component.scss'],
})
export class MainHeaderComponent implements OnInit {
  routeParamSubscription!:Subscription;
  studentProfileId:any;
  constructor(private router:Router,private route:ActivatedRoute) {}

  ngOnInit(): void {
    this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
      console.log(params);
      
     // this.studentProfileId = params.get('id');
     this.studentProfileId = 1;
     // this.Init();
    })
  }
}
