import { Component } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.scss'],
})
export class SideNavComponent {
  routeParamSubscription!:Subscription;
  studentProfileId:any;
  constructor(private router:Router,private route:ActivatedRoute) {}
  isCollapsed: boolean = false;

  ngOnInit(): void {
    this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
      console.log(params);
      
     // this.studentProfileId = params.get('id');
     this.studentProfileId = 1;
     // this.Init();
    })
  }
  
  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
