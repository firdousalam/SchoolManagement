import { Component, HostBinding, OnInit } from '@angular/core';
import {
  admissionPaymentTabs,
  admissionPaymentTabNames,
} from 'src/app/shared/constants/admission.constant';
import { AdmissionServiceService } from 'src/app/shared/services/api/admission-service.service';
@Component({
  selector: 'app-admission',
  templateUrl: './admission.component.html',
  styleUrls: ['./admission.component.scss']
})

export class AdmissionComponent implements OnInit {
  @HostBinding('class') class = 'page-content-area';
  tabs: any;
  selectedTab: string = '';
  admissionPaymentTabNames: any = admissionPaymentTabNames;
  constructor(private api: AdmissionServiceService) {
    this.tabs = admissionPaymentTabs;
   }

  ngOnInit(): void {
    this.selectedTab = admissionPaymentTabNames.feePayment;
  }
  onTabSelect(tabName: any) {
    this.selectedTab = tabName;
  }
}
