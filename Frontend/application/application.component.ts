import { Component, OnInit, SimpleChanges, EventEmitter, Output } from '@angular/core';
import { applicationObjectCreation } from 'src/app/shared/adaptor/adaptor';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { IProfileSearch, Profile } from 'src/app/shared/models/profile';
import { setSession, getSession } from 'src/app/shared/common/storage';
import { isEmpty, toNumber } from 'lodash';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
//import validator and FormBuilder
import { FormGroup, AbstractControl, Validators, FormBuilder } from '@angular/forms';



@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss']
})
export class ApplicationComponent implements OnInit {
  profileSubscription!:Subscription;
  formSubscription!: Subscription;
  applicationStatus: string = '';
  @Output() isFormValid = new EventEmitter<any>();
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
  
  //Create FormGroup
  subscriptionArray: any[] = [];
  form !: FormGroup;
  constructor(private profileService: StudentProfileServiceService, private router: Router, private route: ActivatedRoute, private date:DatePipe ,private formBuilder: FormBuilder) {
   
    //this.myGroup = new FormGroup({ firstName: new FormControl() }); 
    this.form = this.formBuilder.group(
      {
        application_name: ['', Validators.required]
/*,
        signature: ['', Validators.required],
        caddress: ['', Validators.required],
        mobileNumber: ['', [Validators.required]],
        landlineNumber: ['', [Validators.required]],
        alternateMobileNumber: ['', [Validators.required]],
        emailId: ['', [Validators.required, Validators.email]],
        */
      }
    );
   }
   get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }
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

  ngOnChanges(changes: SimpleChanges): void {

    this.formSubscription = this.form.valueChanges.subscribe(() => {
      this.isFormValid.emit(this.form.valid); 
    });
    this.subscriptionArray.push(this.formSubscription);
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
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId)).subscribe((data:any)=>{      
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
