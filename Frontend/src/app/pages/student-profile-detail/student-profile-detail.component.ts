import { Component, OnInit, HostBinding } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import {
  studentProfileTabs,
  studentProfileTabNames,
} from 'src/app/shared/constants/admission.constant';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import {IProfile, IProfilePage, stdEducationList, stdPersonalDetail, stdProfessionList} from 'src/app/shared/models/profile'
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
@Component({
  selector: 'app-student-profile-detail',
  templateUrl: './student-profile-detail.component.html',
  styleUrls: ['./student-profile-detail.component.scss'],
})
export class StudentProfileDetailComponent implements OnInit {
  eventsSubject: Subject<void> = new Subject<void>();

  @HostBinding('class') class = 'page-content-area';
  tabs: any;
  selectedTab: string = '';
  studentProfileTabNames: any = studentProfileTabNames;
  editMode = false;
  studentProfileData:IProfile[]=[];
  profileSubscription!: Subscription;
  routeParamSubscription!:Subscription;
  studentProfileId:any;
  studentEducationData:stdEducationList[]=[];
  studentpPersonalData!:stdPersonalDetail;
  studentProfessionData:stdProfessionList[]=[];
  constructor(private api: StudentProfileServiceService,private router:Router,private route:ActivatedRoute) {
    // this.tabs = studentProfileTabs;
    // this.selectedTab = studentProfileTabNames.PERSONAL
    this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
      console.log(params);
      
      this.studentProfileId = params.get('id');
    })
  }
  onEdit() {
    this.editMode = true;
  }
  onCancel(){
    this.editMode = false;
  }
  ngOnInit(): void {
    this.init();
  }

  submitAllData(){
    this.eventsSubject.next();
    console.log('Amirrrrrrrrrrrrr',this.studentEducationData);
    
  }

  updatedEducationData(data:stdEducationList[]){
    this.studentEducationData = data;
    console.log('sfsdsgdsgdgdg',this.studentEducationData);
    
  }
  // onTabSelect(tabName: any) {
  //   this.selectedTab = tabName;
  // }
init(){
  const request = {profileId:this.studentProfileId}
  this.profileSubscription = this.api.getBySearchCriteria(request).subscribe((data: IProfilePage) => {
    console.log(data);
    this.studentProfileData = data.content;
    this.studentEducationData = this.studentProfileData[0].stdEducationList;
    this.studentProfessionData = this.studentProfileData[0].stdProfessionList;
    this.studentpPersonalData = this.studentProfileData[0].stdPersonalDetail;
  })
}
}
