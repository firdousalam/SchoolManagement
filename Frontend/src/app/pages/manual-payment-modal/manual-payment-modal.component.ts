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
  selector: 'app-manual-payment-modal',
  templateUrl: './manual-payment-modal.component.html',
  styleUrls: ['./manual-payment-modal.component.scss']
})
export class ManualPaymentModalComponent implements OnInit, OnDestroy {
  fileList: NzUploadFile[] = [];
  paymentform!: FormGroup;
  submitted = false;
  makePaymentSubscription!: Subscription;
  fileUploadSubscription!: Subscription;
  subscriptionArr: any[] = [];
  studentProfileId: any;
  attachmentNumber: any = 0;
  constructor(private date: DatePipe, private paymentApi: FeePaymentService, private noti: NotificationService, private modalRef: NzModalRef, private formBuilder: FormBuilder,) {

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
  closeModal() {
    this.modalRef.close();
  }

  beforeUpload = (file: NzUploadFile): boolean => {
    this.fileList = this.fileList.concat(file);
    this.handleUpload();
    return false;
  };

  handleUpload() {
    const formData = new FormData();
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    this.fileList.forEach((file: any) => {
      formData.append('file', file);
    });
    const queryParams = {
      description: 'Application Fee',
      docCategory: 'Application Fee',
      documentType: 'paymentType'
    }
    this.fileUploadSubscription = this.paymentApi.fileUploadWithSearch(queryParams, formData).subscribe((x: any) => {

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
    } else if (this.paymentform.valid && !this.fileList.length) {
      this.noti.showErrorToast('Please select Application fee attachment');
      return;
    }
    const requestData = {
      modeOfPayment: this.paymentform?.value?.paymentType,
      number: this.paymentform?.value?.number.toString(),
      actualPaymentDate: this.date.transform(this.paymentform?.value?.actualPaymentDate, 'dd/MM/yyyy'),
      amount: this.paymentform?.value?.amount,
      remark: this.paymentform?.value?.remark,
      dateofEntry: "",
      paymentType: "Application Fee",
      installment: "",
      paymentDueDate: "",
      docIdList: [
        0
      ],
      tempIdList: [this.attachmentNumber],
      paymentStatus: "Paid",
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