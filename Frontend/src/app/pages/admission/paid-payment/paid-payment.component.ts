import { Component, Input, OnInit } from '@angular/core';
import { paidPaymentColumnDefs } from 'src/app/shared/constants/payment.constant';
@Component({
  selector: 'app-paid-payment',
  templateUrl: './paid-payment.component.html',
  styleUrls: ['./paid-payment.component.scss']
})
export class PaidPaymentComponent implements OnInit {
  @Input()batchData:any;
  adminTableConfig: any[];
  rowData: any[];
  constructor() { 
    this.adminTableConfig = paidPaymentColumnDefs;
    this.rowData =  [
      { 
        type: 'Tution Fees',
        installment: 'Installment 1', 
        amount: 3456,
        date: 'dd/mm/yyyy',
        payment_status: 'Verified'
      },
      { 
        type: 'Couse Fees',
        installment: 'Installment 2', 
        amount: 454,
        date: 'dd/mm/yyyy',
        payment_status: 'Verified'
      },
    ]
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
