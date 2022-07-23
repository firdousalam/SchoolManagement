import { Component, OnInit } from '@angular/core';
import { applicationObjectCreation } from 'src/app/shared/adaptor/adaptor';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { IProfileSearch, IProfile, Profile } from 'src/app/shared/models/profile';
import { setSession, getSession } from 'src/app/shared/common/storage';
import { isEmpty, toNumber } from 'lodash';



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
  studentApplication:any = {
    personalDetails:{},
    stdProfessionList:[],
    stdEducationList:[],
    stdContactDetail:{} 
  }
  educationRecord:any = {};
  //applicationViewDetails: IProfile = Profile;
  applicationViewDetails: any;
  
  
  constructor(private profileService: StudentProfileServiceService, private router: Router) { }

  ngOnInit(): void {        
    this.applicationStatus = !isEmpty(getSession('profileId'))? '4':'1';    
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
      this.applicationStatus = !isEmpty(getSession('profileId'))? '4':'1';   
    }else if(status === "saveAsDraft"){
      this.applicationStatus = "3";
    }
  }
  applicationSubmit(requestData: any){ 
    const userId = Math.floor(Math.random() * (999999 - 100000))?.toString();  
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, userId)).subscribe((data:any)=>{      
      setSession('profileId',data.messageCode);      
      this.applicationStatus = '4';
    },(error:Error)=>{
      console.log(error);      
    })    
  }

  applicationView():void{    
    if(!isEmpty(getSession('profileId'))){
      const profileId = getSession('profileId');
      this.applicationStatus = "2";      
      const params:IProfileSearch = {profileId: toNumber(profileId)};
      this.profileService.getByID(params).subscribe((data:any)=>{       
        //console.log("data",this.applicationViewDetails);
        this.applicationViewDetails = data;
      },(error:Error)=>{
        console.log(error);      
      })
    }else{
      this.applicationStatus = "5";
    }    
  }
  saveDraft(){
    this.saveAsDraft = true;
  }
}
