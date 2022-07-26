import { Component, OnInit } from '@angular/core';
import { applicationObjectCreation } from 'src/app/shared/adaptor/adaptor';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { StudentProfileServiceService } from 'src/app/shared/services/api/student-profile-service.service';
import { DataService } from 'src/app/shared/services/api/data.service';
import { ITypeSearch} from 'src/app/shared/models/type';
import { IProfileSearch, Profile } from 'src/app/shared/models/profile';
import { setSession, getSession } from 'src/app/shared/common/storage';
import { isEmpty, toNumber } from 'lodash';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
import { Content } from '@angular/compiler/src/render3/r3_ast';



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
  referenceDate: any = moment().format('DD/MM/YYYY');
  studentApplication:any = {
    personalDetails:{},
    stdProfessionList:[],
    stdEducationList:[],
    stdContactDetail:{},
    docList: [] 
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
  editDraft:any;
  pageTraverseStatus: string = 'APPLICATION_STARTED'
  documentTypeList:any;
  constructor(private profileService: StudentProfileServiceService, 
    private router: Router, 
    private route: ActivatedRoute, 
    private date:DatePipe,
    private  dataService: DataService) { }

  async ngOnInit(): Promise<any> {            
    //const userId = this.route.snapshot.paramMap.get('id')?.toString(); 
    const userId = '1';   
    const params:IProfileSearch = {userId: toNumber(userId)};
    let studentProfile = await this.profileService.getProfileByUserID(params).toPromise(); 
    this.pageTraverseStatus = this.pageCheck(studentProfile);   
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
    this.loadDocumentTypeList();
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
    //const userId = this.route.snapshot.paramMap.get('id')?.toString();
    const userId = '1';
    const params:IProfileSearch = {userId: toNumber(userId)};
    let studentProfile = await this.profileService.getProfileByUserID(params).toPromise(); 
    if(status === "applicationForm"){      
      try{        
        if(!isEmpty(studentProfile)){
          this.pageTraverseStatus = this.pageCheck(studentProfile);   
          if(this.pageTraverseStatus === 'AWAITING_APPROVAL'){
            this.applicationStatus = '4';
          }else if(this.pageTraverseStatus === 'DRAFT'){
            this.applicationStatus = '7';
          }          
        }else{
          this.applicationStatus = '1';
        }
      }
      catch(error){
        console.log(error);
      }         
    }else if(status === "saveAsDraft"){        
      if(!isEmpty(studentProfile)){        
        this.pageTraverseStatus = this.pageCheck(studentProfile);   
        if(this.pageTraverseStatus === 'DRAFT'){
          this.applicationStatus = "3";
          this.saveAsDraft = true;
          this.editDraft = studentProfile; 
        }else{
          this.applicationStatus = "6";  
        }
      }else{
        this.applicationStatus = "6";        
      }     
    }
  }
  //APPLICATION SUBMITTED
  applicationSubmit(requestData: any){ 
    console.log("requestData::",requestData);
    this.userId = this.route.snapshot.paramMap.get('id')?.toString();
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId, 'Awaiting Approval')).subscribe((data:any)=>{      
      setSession('profileId',data.messageCode);  
      setSession('userId',this.userId)          
      this.applicationStatus = '4';
    },(error:Error)=>{
      console.log(error);      
    })    
  }
  //APPLICATION SUBMITTED POST DRAFT
  applicationSubmittedPostDraft(requestData:any){
    this.userId = this.route.snapshot.paramMap.get('id')?.toString();
    this.profileSubscription = this.profileService.update(applicationObjectCreation(requestData, this.userId, 'Awaiting Approval')).subscribe((data:any)=>{      
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
          this.pageTraverseStatus = this.pageCheck(studentProfile);   
          if(this.pageTraverseStatus === 'DRAFT' || this.pageTraverseStatus === 'APPLICATION_STARTED'){
            this.applicationStatus = "5";
          }else{
            this.application = studentProfile;          
            this.personal = studentProfile?.stdPersonalDetail;
            this.contact = studentProfile?.stdContactDetail;
            this.profession = studentProfile?.stdProfession;
            this.education = studentProfile?.stdEducationList;
            this.docList = studentProfile?.docList;
            this.admission = studentProfile?.stdAdmission;
            this.applicationStatus = "2"; 
          }    
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

  //APPLICATION SUBMITTED AS A SAVE AS DRAFT
  applicationDraft(requestData: any){    
    //this.userId = this.route.snapshot.paramMap.get('id')?.toString();    
    this.userId = '1';   
    this.profileSubscription = this.profileService.create(applicationObjectCreation(requestData, this.userId, 'Saved')).subscribe((data:any)=>{      
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

  documntUpload(event: any):any {
    console.log(event.target.files[0]);
    //this.file = event.target.files[0];

  }
  pageCheck(studentProfile: any):string{
    if(!isEmpty(studentProfile)){
      if(studentProfile.application.applicationState === 'Awaiting Approval'){
        return 'AWAITING_APPROVAL';
      }else{
        return 'DRAFT';
      }
    }else{
      return 'APPLICATION_STARTED';
    }
  }
  async loadDocumentTypeList(): Promise<any>{    
    const referenceType: ITypeSearch  = {referenceType:'Application Documents', pageSize:20};
    const documentTypeList = await this.dataService.getBySearchCriteria(referenceType).toPromise();
    if(!isEmpty(documentTypeList?.content)){
      this.documentTypeList = documentTypeList.content; 
    }    
  }
}
