import { ITableViewConfig } from "../models/table-view";

export const queryColumnDefs:ITableViewConfig[]=[
    { field: 'id',header: 'Reference ID'},
    { field: 'dateofEntry', header: 'Date' },
    { field: 'type', header: 'Type' },
    { field: 'queryOrClarification', header: 'Query/Clarification' },
    { field: 'queryStatus', header: 'Status' },
    { field: 'response', header: 'Response' },
  ];