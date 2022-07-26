import { Component, OnInit } from '@angular/core';
import { applicationObjectCreation } from 'src/app/shared/adaptor/adaptor';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { IProfileSearch, Profile } from 'src/app/shared/models/profile';
import { setSession, getSession } from 'src/app/shared/common/storage';
import { isEmpty, toNumber } from 'lodash';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
import { NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
import { PaymentModalComponent } from '../payment-modal/payment-modal.component';



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
  referenceDate: any = moment().format('DD-MM-YYYY');
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
  personal:any;
  application:any;
  contact:any;
  profession: any;
  education:any;
  admission:any;
  docList:any;
  
  
  constructor(private modal:NzModalService,private profileService: StudentProfileServiceService, private router: Router, private route: ActivatedRoute, private date:DatePipe ) { }

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

  async applicationStatusChange(status: string): Promise<any> {
    if(status === "applicationForm"){
      const userId = this.route.snapshot.paramMap.get('id')?.toString();
      const params:IProfileSearch = {userId: toNumber(userId)};
      try{
        const studentProfile = await this.profileService.getProfileByUserID(params).toPromise();        
        if(!isEmpty(studentProfile)){
          this.applicationStatus = '4';
        }else{
          this.applicationStatus = '1';
        }
      }
      catch(error){
        console.log(error);
      }
      // if(!isEmpty(studentProfile)){
      //   this.applicationStatus = '4';
      // }else{
      //  this.applicationStatus = '1';
      // }
      //this.applicationStatus = this.userCheck('1', '4');       
    }else if(status === "saveAsDraft"){
      this.applicationStatus = "3";
    }
  }
  applicationSubmit(requestData: any){ 
    this.userId = this.route.snapshot.paramMap.get('id')?.toString();
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId, 'SUBMITTED')).subscribe((data:any)=>{      
      setSession('profileId',data.messageCode);  
      setSession('userId',this.userId)          
      this.applicationStatus = '4';
    },(error:Error)=>{
      console.log(error);      
    })    
  }

  async applicationView():Promise<any>{
    const userId = this.route.snapshot.paramMap.get('id')?.toString();      
    if(!isEmpty(userId)){        
      const params:IProfileSearch = {userId: toNumber(userId)};      
      try{        
        const studentProfile = await this.profileService.getProfileByUserID(params).toPromise();        
        if(!isEmpty(studentProfile)){
          this.application = studentProfile;          
          this.personal = studentProfile?.stdPersonalDetail;
          this.contact = studentProfile?.stdContactDetail;
          this.profession = studentProfile?.stdProfession;
          this.education = studentProfile?.stdEducationList;
          this.docList = studentProfile?.docList;
          this.admission = studentProfile?.stdAdmission;
          this.applicationStatus = "2"; 
        }else{
          this.applicationStatus = "5";
        }      
      }catch(error){
        console.log(error);
      }           
    }else{
      this.applicationStatus = "5";
    }    
  }

  applicationDraft(requestData: any){    
    this.userId = this.route.snapshot.paramMap.get('id')?.toString();
    console.log("create::",applicationObjectCreation(requestData, this.userId, 'DRAFTED'));
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId, 'DRAFTED')).subscribe((data:any)=>{      
      setSession('profileId',data.messageCode);  
      setSession('userId',this.userId)          
      this.applicationStatus = '3';
      this.saveAsDraft = true;
    },(error:Error)=>{
      console.log(error);      
    })    
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

  showPopUp(){
    this.modal.create({
      nzTitle: '',
      nzContent:  PaymentModalComponent,
      nzFooter: null,
      nzClosable: false,
      nzMaskClosable: false,
  })
  }
}


