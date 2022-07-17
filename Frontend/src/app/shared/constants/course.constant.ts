import { ITableViewConfig } from "../models/table-view";
export const courseColumnDefs:ITableViewConfig[]=[
    // Using dot notation to access nested property
    { field: 'id', header: 'SI. No.' },
    { field: 'subject', header: 'Subject' },
    { field: 'heading', header: 'Document Name' },
    { field: 'description', header: 'Description' },
    { field: 'fromDate', header: 'Date' },
    { field: 'malData.docName', header: 'Malayalam' },
    { field: 'engData.docName', header: 'English' }
  ];