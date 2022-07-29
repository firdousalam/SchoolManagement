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
export class PersonalHeaderDetailsComponent implements OnInit, OnChanges,OnDestroy {
  @Input() editMode: boolean = false;
  @Input() studentPersonalData!: stdPersonalDetail;
  @Output() employmentStatusChange = new EventEmitter<any>();
  @Output() profileImageChange = new EventEmitter<any>();
  employmentStatus: any;
  fileList: NzUploadFile[] = [];
  previewUrl: any = null;
  fileUploadProgress!: string;
  fileUploadSubscription!:Subscription;
  uploadedFilePath!: string;
  constructor(private noti:NotificationService,private headerpersonalApi: ProfileHeaderService) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.employmentStatus = this.studentPersonalData?.employmentStatus;
    this.editMode ? '':(this.previewUrl='');
  }

  beforeUpload = (file: NzUploadFile): boolean => {
    this.fileList = this.fileList.concat(file);
    this.preview();
    this.handleChange();
    return false;
  };
  handleChange(){
    const formData = new FormData();
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    this.fileList.forEach((file: any) => {
      formData.append('file', file);
    });
     
    this.fileUploadSubscription = this.headerpersonalApi.fileUpload(formData).subscribe((x: any) => {      
      if (x.status === 201) {
        this.profileImageChange.emit(x.body);
        this.noti.showSuccessToast('Image uploaded successfully')
      }
    });
    
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
  //  this.fileUploadSubscription.unsubscribe();
  }
}
