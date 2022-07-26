import { Component, HostBinding, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/core/services/notification.service';
import {
  admissionPaymentTabs,
  admissionPaymentTabNames,
} from 'src/app/shared/constants/admission.constant';
import { AdmissionServiceService } from 'src/app/shared/services/api/admission-service.service';
import { BatchDetailsService } from 'src/app/shared/services/api/batch-details.service';
import { CommonService } from 'src/app/shared/services/api/common.service';
import { FeePaymentService } from 'src/app/shared/services/api/fee-payment.service';
@Component({
  selector: 'app-admission',
  templateUrl: './admission.component.html',
  styleUrls: ['./admission.component.scss']
})

export class AdmissionComponent implements OnInit, OnDestroy {
  @HostBinding('class') class = 'page-content-area';
  tabs: any;
  selectedTab: string = '';
  batchSubscription!:Subscription;
  routeParamSubscription:Subscription | undefined;
  paymentSubscription!:Subscription;
  makePaymentSubscription!:Subscription;
  subscriptionArr:any=[];
  studentProfileId:any;
  batchData:any;
  paidPayments:any;
  unpaidPayments:any;
  submittedRequestData:any;
  admissionPaymentTabNames: any = admissionPaymentTabNames;
  constructor(private commonService:CommonService,private paymentApi:FeePaymentService,private noti:NotificationService,private route:ActivatedRoute,private api: AdmissionServiceService,private bacthApi:BatchDetailsService) {
    this.tabs = admissionPaymentTabs;
  //  this.routeParamSubscription = this.route.paramMap.subscribe((params: ParamMap) => {
    const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
    this.studentProfileId = studentProfileObj.studentProfileId;
      this.commonService.profileSubject.next({profileId:this.studentProfileId});
   // })
    //this.subscriptionArr.push(this.routeParamSubscription);
   }

  ngOnInit(): void {
    this.selectedTab = admissionPaymentTabNames.feePayment;
    this.batchSubscription = this.bacthApi.getByID({profileId:this.studentProfileId}).subscribe((data:any)=>{
      this.batchData = data;
    })
    this.subscriptionArr.push(this.batchSubscription);
    this.paymentSubscription = this.paymentApi.getByID({profileId:this.studentProfileId}).subscribe((data:any)=>{
      this.paidPayments = data?.filter((x: { paymentStatus: string; })=>x.paymentStatus==='Paid');
      this.unpaidPayments =data?.filter((x: { paymentStatus: string; })=>x.paymentStatus!=='Paid');
      console.log(this.paidPayments,this.unpaidPayments);
      this.submittedRequestData = this.unpaidPayments;
    })
    this.subscriptionArr.push(this.paymentSubscription);
  }
  getMakePaymentData(data:any){
    console.log('Hi I am data',data);
    this.submittedRequestData = data;
  }
  onTabSelect(tabName: any) {
    this.selectedTab = tabName;
  }
  makePayment(){
    console.log(this.submittedRequestData);
    
    this.makePaymentSubscription = this.paymentApi.createWithQueryParam({profileId:this.studentProfileId},this.submittedRequestData).subscribe((x:any)=>{
      this.noti.showSuccessToast('Payment has been made successfully');
      this.ngOnInit();
    })
    this.subscriptionArr.push(this.makePaymentSubscription);
  }

  ngOnDestroy(): void {
    this.subscriptionArr.forEach((x:any)=>x.unsubscribe());
  }
}
