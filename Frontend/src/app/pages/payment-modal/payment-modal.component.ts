import { Component, OnInit } from '@angular/core';
import { NzModalRef, NzModalService } from 'ng-zorro-antd/modal';
import { ManualPaymentModalComponent } from '../manual-payment-modal/manual-payment-modal.component';

@Component({
  selector: 'app-payment-modal',
  templateUrl: './payment-modal.component.html',
  styleUrls: ['./payment-modal.component.scss']
})
export class PaymentModalComponent implements OnInit {

  constructor(private modalRef: NzModalRef, private modal: NzModalService) { }

  ngOnInit(): void {
  }
  closeModal() {
    this.modalRef.close();
  }
  openManual() {
    this.modalRef.close();
    this.modal.create({
      nzTitle: '',
      nzContent: ManualPaymentModalComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzClosable: false,
      nzWidth: '800px'
    })
  }
}
