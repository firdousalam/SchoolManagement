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
  routeParamSubscription!: Subscription;
  profileSubscription!: Subscription;
  toggleMenuSubscription!: Subscription;
  menuShowhidSub!: Subscription;
  subArray: any = [];
  studentProfileId: any;
  profileExist: any;
  constructor(private router: Router, private route: ActivatedRoute, private commonService: CommonService) { }
  isCollapsed: boolean = false;


  ngOnInit(): void {

    this.profileExist = localStorage.getItem('profileExist');
    this.profileSubscription = this.commonService.profileSubject.subscribe((x: any) => {
      this.studentProfileId = x.profileId;

    })
    this.subArray.push(this.profileSubscription);

    this.toggleMenuSubscription = this.commonService.menuToggleSubject?.subscribe((x: any) => {
      this.toggleCollapsed();
    })
    this.subArray.push(this.toggleMenuSubscription);
    this.menuShowhidSub = this.commonService.menushowhideSubject.subscribe((x: any) => {
      this.profileExist = x;
    });
    this.subArray.push(this.menuShowhidSub);
  }

  isOpen() {
    return this.router.url === '/dashboard' ? false : true;
  }
  ngOnDestroy(): void {
    this.subArray.array.forEach((element: any) => element.unsubscribe());
  }
  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }
}
