import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { feePaymentColumnDefs } from 'src/app/shared/constants/payment.constant';
@Component({
  selector: 'app-fee-payment',
  templateUrl: './fee-payment.component.html',
  styleUrls: ['./fee-payment.component.scss']
})
export class FeePaymentComponent implements OnInit,OnChanges {
  @Input()batchData:any;
  @Input()unpaidPayments:any;
  @Output() makePaymentData = new EventEmitter();
  adminTableConfig: any[];
  rowData!: any[];
  checked = true;
  indeterminate = true;
  totalAmount:any;
  setOfCheckedId = new Set<number>();
  listOfCurrentPageData: readonly any[] = [];
  constructor() { 
    this.adminTableConfig = feePaymentColumnDefs;
  } 

  ngOnChanges(changes: SimpleChanges): void {
    this.rowData = this.unpaidPayments;
    if(this.rowData?.length){
      this.listOfCurrentPageData = this.rowData;
      this.listOfCurrentPageData?.forEach(item => this.updateCheckedSet(item.id, true));
      this.refreshCheckedStatus();
    }
    this.getTotalAmount();
    
  }
  updateCheckedSet(id: any, checked: boolean): void {
    if (checked) {
      this.getTotalAmount();
      this.setOfCheckedId.add(id);
    } else {
      this.calCulateAmount(id);
      this.setOfCheckedId.delete(id);
    }
  }
  calCulateAmount(id:any){
    const updatedArr:[] = JSON.parse(JSON.stringify(this.rowData));
   
    const filterArr = updatedArr.filter((x:any)=> x.id!==id);
    this.getTotalAmount(filterArr);
  }
  onItemChecked(id: any, checked: boolean): void {

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
  disabledCheckBox(data:any){
    if(this.rowData?.length>1){
      return (data.installment !== 'Installment 1' ? false:true);
    }else if(this.rowData?.length===1){
      return true;
    }else{
      return false;
    }
  }
  getTotalAmount(newArr?:any){ 
    const updatedArrTotal:any = newArr?.length ? newArr : this.rowData;
    this.makePaymentData.emit(updatedArrTotal);
    this.totalAmount = updatedArrTotal?.length ?  (updatedArrTotal
      ?.map((t:any)=> parseInt(t?.amount))
      ?.reduce((acc:any,cur:any)=> acc+cur)
      ):'';
    }
  }


