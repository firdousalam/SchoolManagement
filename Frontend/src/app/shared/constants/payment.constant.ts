import { ITableViewConfig } from "../models/table-view";

export const feePaymentColumnDefs:ITableViewConfig[]=[
    // Using dot notation to access nested property
    { field: 'type', header: '' },
    { field: 'installment', header: '' },
    { field: 'amount', header: 'Amount' },
    { field: 'action', header: '' },
  ];
export const paidPaymentColumnDefs:ITableViewConfig[]=[
    // Using dot notation to access nested property
    { field: 'type', header: '' },
    { field: 'installment', header: '' },
    { field: 'amount', header: 'Amount' },
    { field: 'date', header: 'Payment Date' },
    { field: 'payment_status', header: 'Status' },
  ];