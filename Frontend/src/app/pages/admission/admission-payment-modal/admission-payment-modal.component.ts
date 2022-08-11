import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl, FormBuilder, AbstractControl } from '@angular/forms';
import * as moment from 'moment';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { NzUploadFile } from 'ng-zorro-antd/upload';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/core/services/notification.service';
import { FeePaymentService } from 'src/app/shared/services/api/fee-payment.service';

@Component({
  selector: 'app-admission-payment-modal',
  templateUrl: './admission-payment-modal.component.html',
  styleUrls: ['./admission-payment-modal.component.scss']
})
export class AdmissionPaymentModalComponent implements OnInit {
  fileList: NzUploadFile[] = [];
  paymentform!: FormGroup;
  submitted = false;
  makePaymentSubscription!: Subscription;
  fileUploadSubscription!: Subscription;
  subscriptionArr: any[] = [];
  studentProfileId: any;
  attachmentNumber: any=0;
  constructor(private modalRef: NzModalRef,private date: DatePipe, private paymentApi: FeePaymentService, private noti: NotificationService, private formBuilder: FormBuilder) {

  }

  ngOnInit(): void {
    const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
    this.studentProfileId = studentProfileObj.studentProfileId;
    this.paymentform = this.formBuilder.group(
      {
        paymentType: ['', Validators.required],
        number: ['', Validators.required],
        actualPaymentDate: ['', Validators.required],
        amount: ['', Validators.required],
        remark: ['', Validators.required],
      }
    );
  } 

  beforeUpload = (file: NzUploadFile): boolean => {
    this.fileList = this.fileList.concat(file);
    this.handleUpload();
    return false;
  };
  closeModal() {
    this.modalRef.close();
  }
  handleUpload() {
    const formData = new FormData();
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    this.fileList.forEach((file: any) => {
      formData.append('file', file);
    });    
    const queryParams={
      description: 'Admission Fee',
      docCategory: 'Admission Fee',
      documentType: 'paymentType'
     }
    this.fileUploadSubscription = this.paymentApi.fileUploadWithSearch(queryParams,formData).subscribe((x: any) => {

        this.attachmentNumber = x;

    });
    this.subscriptionArr.push(this.fileUploadSubscription);
  }


  get f(): { [key: string]: AbstractControl } {
    return this.paymentform.controls;
  }


  onSubmit(): void {

    this.submitted = true;
    if (this.paymentform.invalid) {
      
      return;
    }else if(this.paymentform.valid && !this.fileList.length){
      this.noti.showErrorToast('Please select Admission fee attachment');
      return;
    }
    const requestData = {
      modeOfPayment: this.paymentform?.value?.paymentType,
      number: this.paymentform?.value?.number.toString(),
      actualPaymentDate: this.date.transform(this.paymentform?.value?.actualPaymentDate, 'dd/MM/yyyy'),
      amount: this.paymentform?.value?.amount,
      remark: this.paymentform?.value?.remark,
      dateofEntry: "",
      paymentType: "Admission Fee",
      installment: "",
      docIdList: [
        0
      ],
      paymentDueDate: "",
      paymentStatus: "Paid",
      tempIdList: [this.attachmentNumber],
      profileId: this.studentProfileId
    }
    this.makePaymentSubscription = this.paymentApi.createWithQueryParam({ profileId: this.studentProfileId }, [requestData]).subscribe((x: any) => {
      this.noti.showSuccessToast('Payment has been made successfully'); 
      this.closeModal();
    })
    this.subscriptionArr.push(this.makePaymentSubscription);
  }

  ngOnDestroy(): void {
    this.subscriptionArr.forEach((x: any) => x.unsubscribe());
  }
}