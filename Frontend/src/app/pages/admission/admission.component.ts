import { Component, HostBinding, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subscription } from 'rxjs';
import {
  admissionPaymentTabs,
  admissionPaymentTabNames,
} from 'src/app/shared/constants/admission.constant';
import { AdmissionServiceService } from 'src/app/shared/services/api/admission-service.service';
import { BatchDetailsService } from 'src/app/shared/services/api/batch-details.service';
import { FeePaymentService } from 'src/app/shared/services/api/fee-payment.service';
@Component({
  selector: 'app-admission',
  templateUrl: './admission.component.html',
  styleUrls: ['./admission.component.scss']
})

export class AdmissionComponent implements OnInit {
  @HostBinding('class') class = 'page-content-area';
  tabs: any;
  selectedTab: string = '';
  batchSubscription!:Subscription;
  routeParamSubscription!:Subscription;
  paymentSubscription!:Subscription;
  studentProfileId:any;
  batchData:any;
  paidPayments:any;
  unpaidPayments:any;
  admissionPaymentTabNames: any = admissionPaymentTabNames;
  constructor(private paymentApi:FeePaymentService,private route:ActivatedRoute,private api: AdmissionServiceService,private bacthApi:BatchDetailsService) {
    this.tabs = admissionPaymentTabs;
    this.routeParamSubscription = this.route.paramMap.subscribe((params: ParamMap) => {
      console.log(params);

      this.studentProfileId = params.get('id');
    })
   }

  ngOnInit(): void {
    this.selectedTab = admissionPaymentTabNames.feePayment;
    this.batchSubscription = this.bacthApi.getByID({profileId:this.studentProfileId}).subscribe((data:any)=>{
      this.batchData = data;
    })
    this.paymentSubscription = this.paymentApi.getByID({profileId:this.studentProfileId}).subscribe((data:any)=>{
      this.paidPayments = data?.filter((x: { paymentStatus: string; })=>x.paymentStatus==='Paid');
      this.unpaidPayments =data?.filter((x: { paymentStatus: string; })=>x.paymentStatus!=='Paid');
      console.log(this.paidPayments,this.unpaidPayments);
      
    })
  }
  onTabSelect(tabName: any) {
    this.selectedTab = tabName;
  }
}
