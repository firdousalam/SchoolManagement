import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { feePaymentColumnDefs } from 'src/app/shared/constants/payment.constant';
@Component({
  selector: 'app-fee-payment',
  templateUrl: './fee-payment.component.html',
  styleUrls: ['./fee-payment.component.scss']
})
export class FeePaymentComponent implements OnInit,OnChanges {
  @Input()batchData:any;
  @Input()unpaidPayments:any;
  adminTableConfig: any[];
  rowData!: any[];
  checked = true;
  indeterminate = true;
  setOfCheckedId = new Set<number>();
  listOfCurrentPageData: readonly any[] = [];
  constructor() { 
    this.adminTableConfig = feePaymentColumnDefs;
  } 

  ngOnChanges(changes: SimpleChanges): void {
    this.rowData = this.unpaidPayments;
    if(this.rowData){
      this.listOfCurrentPageData = this.rowData;
      this.listOfCurrentPageData?.forEach(item => this.updateCheckedSet(item.id, true));
      this.refreshCheckedStatus();
    }
    
  }
  updateCheckedSet(id: any, checked: boolean): void {
    if (checked) {
      console.log('amir');
      this.rowData.forEach((x:any,i)=>{
        if(x.id === id){
          this.rowData[i].paymentStatus = "Paid"
        }
      })
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
    this.checked = this.listOfCurrentPageData.every(item => this.setOfCheckedId.has(item.id));
    this.indeterminate = this.listOfCurrentPageData.some(item => this.setOfCheckedId.has(item.id)) && !this.checked;
  }
  onCurrentPageDataChange($event: readonly any[]): void {
    this.listOfCurrentPageData = $event;
    this.refreshCheckedStatus();
    
  }
  ngOnInit(): void {

  }
  getTotalAmount(){
      return (this.rowData
        ?.map((t:any)=> parseInt(t?.amount))
        ?.reduce((acc:any,cur:any)=> acc+cur)
        );
    }
  }


