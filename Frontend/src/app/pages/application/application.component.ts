import { Component, OnInit } from '@angular/core';
import { applicationObjectCreation } from 'src/app/shared/adaptor/adaptor';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { IProfileSearch } from 'src/app/shared/models/profile';
import { setSession, getSession } from 'src/app/shared/common/storage';
import { isEmpty, toNumber } from 'lodash';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';



@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss']
})
export class ApplicationComponent implements OnInit {
  profileSubscription!:Subscription;
  applicationStatus: string = '';
  secondary: boolean = false;
  heading: string = 'Dashboard';
  applicationSubmitted: boolean = false;
  saveAsDraft: boolean = false;
  model:any;
  userId: any;
  referenceDate: any = moment().format('YYYY-M-D');
  studentApplication:any = {
    personalDetails:{},
    stdProfessionList:[],
    stdEducationList:[],
    stdContactDetail:{} 
  }
  educationRecord:any = {};
  //applicationViewDetails: IProfile = Profile;
  applicationViewDetails: any;
  professionalDetailsShow: boolean = false;
  application:any;
  personalDetails:any;
  
  
  constructor(private profileService: StudentProfileServiceService, private router: Router, private route: ActivatedRoute, private date:DatePipe ) { }

  ngOnInit(): void {            
    this.applicationStatus = this.userCheck('1', '4');         
    this.educationRecord = {  
      courseName: "",
      institution: "",                              
      percentage:"",
      yearofCompletion:"",                             
      educationStatus: "",                              
      sequence: 0  
    }; 
    this.studentApplication.stdEducationList.push(this.educationRecord);             
  }

  addRow() {        
    this.educationRecord = {  
                              courseName: "",
                              institution: "",                              
                              percentage:"",
                              yearofCompletion:"",                             
                              educationStatus: "",                              
                              sequence: 0
                        }; 
    this.studentApplication.stdEducationList = [...this.studentApplication.stdEducationList,this.educationRecord]     
    return true;
  }  

  applicationStatusChange(status: string): void {
    if(status === "applicationForm"){
      this.applicationStatus = this.userCheck('1', '4');       
    }else if(status === "saveAsDraft"){
      this.applicationStatus = "3";
    }
  }
  applicationSubmit(requestData: any){ 
    this.userId = this.route.snapshot.paramMap.get('id')?.toString();    
    //setSession('userId',userId?.toString());
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId)).subscribe((data:any)=>{      
      setSession('profileId',data.messageCode);  
      setSession('userId',this.userId)          
      this.applicationStatus = '4';
    },(error:Error)=>{
      console.log(error);      
    })    
  }

  applicationView():void{   
    this.applicationStatus = this.userCheck('5', '2');      
    if(!isEmpty(getSession('profileId'))){
      const profileId = getSession('profileId');            
      const params:IProfileSearch = {profileId: toNumber(profileId)};
      const promise = this.profileService.getByID(params).toPromise();
      promise.then((data)=>{
        this.application =  data;        
      }).catch((error)=>{
        console.log("Promise rejected with " + JSON.stringify(error));
      });      
    }else{
      this.applicationStatus = "5";
    }    
  }
  saveDraft(){
    this.saveAsDraft = true;
  }

  sameCorrespondingAddress(check: string){         
    if(check === 'YES'){
      this.studentApplication.stdContactDetail.paddress = this.studentApplication.stdContactDetail.caddress
      this.studentApplication.stdContactDetail.pdistrict = this.studentApplication.stdContactDetail.cdistrict;
      this.studentApplication.stdContactDetail.pstate = this.studentApplication.stdContactDetail.cstate;
      this.studentApplication.stdContactDetail.ppinCode = this.studentApplication.stdContactDetail.cpinCode; 
    }else{
      this.studentApplication.stdContactDetail.paddress = null;
      this.studentApplication.stdContactDetail.pdistrict = null;
      this.studentApplication.stdContactDetail.pstate = null;
      this.studentApplication.stdContactDetail.ppinCode = null;
    }    
  }  

  userCheck(page1: any, page2: any): any{     
    if(!isEmpty(getSession('userId'))){     
      if(!isEmpty(getSession('profileId'))){
        this.userId = this.route.snapshot.paramMap.get('id')?.toString();
        if(this.userId !== getSession('userId')){
          return page1;
        }else{
          return page2;
        }
      }else{
        return page1;
      }
    }else{
      return page1;
    }
  }
}
