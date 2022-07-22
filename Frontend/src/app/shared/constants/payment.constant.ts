import { ITableViewConfig } from "../models/table-view";

export const feePaymentColumnDefs:ITableViewConfig[]=[
    // Using dot notation to access nested property
    { field: 'paymentType', header: '' },
    { field: 'installment', header: '' },
    { field: 'amount', header: 'Amount' },
    { field: 'action', header: '' },
  ];
export const paidPaymentColumnDefs:ITableViewConfig[]=[
    // Using dot notation to access nested property
    { field: 'paymentType', header: '' },
    { field: 'installment', header: '' },
    { field: 'amount', header: 'Amount' },
    { field: 'actualPaymentDate', header: 'Payment Date' },
    { field: 'paymentStatus', header: 'Status' },
  ];