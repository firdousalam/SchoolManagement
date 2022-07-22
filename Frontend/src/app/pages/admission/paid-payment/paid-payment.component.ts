import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { paidPaymentColumnDefs } from 'src/app/shared/constants/payment.constant';
@Component({
  selector: 'app-paid-payment',
  templateUrl: './paid-payment.component.html',
  styleUrls: ['./paid-payment.component.scss']
})
export class PaidPaymentComponent implements OnInit,OnChanges {
  @Input()batchData:any;
  @Input()paidPayments:any;
  adminTableConfig: any[];
  rowData!: any[];
  constructor() { 
    this.adminTableConfig = paidPaymentColumnDefs; 
  }

  ngOnInit(): void {
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.rowData = this.paidPayments;
  }
  getTotalAmount(){
    return (this.rowData?.length &&this.rowData
      .map((t:any)=> parseInt(t.amount))
      .reduce((acc:any,cur:any)=> acc+cur)
      );
  }
}
