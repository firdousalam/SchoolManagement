import { Component, Input, OnInit } from '@angular/core';
import { feePaymentColumnDefs } from 'src/app/shared/constants/payment.constant';
@Component({
  selector: 'app-fee-payment',
  templateUrl: './fee-payment.component.html',
  styleUrls: ['./fee-payment.component.scss']
})
export class FeePaymentComponent implements OnInit {
  @Input()batchData:any;
  adminTableConfig: any[];
  rowData: any[];
  checked = false;
  indeterminate = false;
  setOfCheckedId = new Set<number>();
  listOfCurrentPageData: readonly any[] = [];
  constructor() { 
    this.adminTableConfig = feePaymentColumnDefs;
    this.rowData =  [
      { 
        type: 'Tution Fees',
        installment: 'Installment 1', 
        amount: 3456,
        action: ''
      },
      { 
        type: 'Couse Fees',
        installment: 'Installment 2', 
        amount: 454,
        action: ''
      },
    ];
    this.listOfCurrentPageData = this.rowData;
    this.listOfCurrentPageData.forEach(item => this.updateCheckedSet(item.type, true));
    this.refreshCheckedStatus();
  } 
  updateCheckedSet(id: any, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }
  onItemChecked(id: any, checked: boolean): void {
    console.log(id,checked);
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }
  refreshCheckedStatus(): void {
    this.checked = this.listOfCurrentPageData.every(item => this.setOfCheckedId.has(item.type));
    this.indeterminate = this.listOfCurrentPageData.some(item => this.setOfCheckedId.has(item.type)) && !this.checked;
  }
  onCurrentPageDataChange($event: readonly any[]): void {
    this.listOfCurrentPageData = $event;
    this.refreshCheckedStatus();
    
  }
  ngOnInit(): void {

  }
  getTotalAmount(){
    return (this.rowData
    .map((t:any)=> t.amount)
    .reduce((acc:any,cur:any)=> acc+cur)
    );
  }

}
