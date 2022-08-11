import { Component, OnInit, Input, OnChanges, SimpleChanges, EventEmitter, Output, OnDestroy } from '@angular/core';
import { NzUploadFile } from 'ng-zorro-antd/upload';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/core/services/notification.service';
import { stdPersonalDetail } from 'src/app/shared/models/profile';
import { ProfileHeaderService } from 'src/app/shared/services/api/profile-header.service';

@Component({
  selector: 'app-personal-header-details',
  templateUrl: './personal-header-details.component.html',
  styleUrls: ['./personal-header-details.component.scss'],
})
export class PersonalHeaderDetailsComponent implements OnInit, OnChanges, OnDestroy {
  @Input() editMode: boolean = false;
  @Input() studentPersonalData!: stdPersonalDetail;
  @Output() employmentStatusChange = new EventEmitter<any>();
  @Output() profileImageChange = new EventEmitter<any>();
  employmentStatus: any;
  fileList: NzUploadFile[] = [];
  previewUrl: any = null;
  fileUploadProgress!: string;
  fileUploadSubscription!:Subscription;
  profilePicSubscription!:Subscription;
  subArr:any[]=[];
  profilePicFromUrl:any;
  uploadedFilePath!: string;
  constructor(private noti:NotificationService,private headerpersonalApi: ProfileHeaderService,) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.employmentStatus = this.studentPersonalData?.employmentStatus;
    this.editMode ? '':(this.previewUrl='');
    // if(this.studentPersonalData?.pictureLink){
    //   this.profilePicSubscription = this.headerpersonalApi.getDoc(parseInt(this.studentPersonalData?.pictureLink)).subscribe((data:any)=>{

    //   });
    //   this.subArr.push(this.profilePicSubscription);
    // }
  }

  beforeUpload = (file: NzUploadFile): boolean => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      this.noti.showErrorToast('You can only upload image file!'); 
      return false;
    }
    this.fileList = this.fileList.concat(file);
    this.preview();
    this.handleChange();
    return false;
  };
  handleChange(){
    const formData = new FormData();
    console.log(this.fileList.slice(-1)[0].type);
    
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    this.fileList.forEach((file: any) => {
      formData.append('file', file);
    });
     const queryParams={
      description: 'Profile Image',
      docCategory: 'Picture',
      documentType: 'StudentPicture'
     }
    this.fileUploadSubscription = this.headerpersonalApi.fileUploadWithSearch(queryParams,formData).subscribe((x: any) => {
      console.log('Profile',x);
        this.profileImageChange.emit(x);
       // this.noti.showSuccessToast('Image uploaded successfully,Please save or Submit to proceed further')
    });
    this.subArr.push(this.fileUploadSubscription);
  }
  preview() {
    // Show preview 
    let mimeType:any = this.fileList[0].type;
    if (mimeType.match(/image\/*/) == null) {
      return;
    }

    let reader:any = new FileReader();
    reader.readAsDataURL(this.fileList[0]);
    reader.onload = (_event: any) => {
      this.previewUrl = reader.result;
    }
  }
 
  radioValueCheck(status: any) {
    this.employmentStatus = status;
 
    this.employmentStatusChange.emit(this.employmentStatus);
  }
  ngOnInit(): void { }

  ngOnDestroy(): void {
    this.subArr.forEach((x:any)=>x.unsubscribe());
  }
}
