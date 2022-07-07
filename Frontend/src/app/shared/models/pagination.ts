export interface IPagination {
  pageNumber: number;
  pageSize: number;
}

export const pageSearch = <IPagination>{
  pageNumber: 0,
  pageSize: 0,
};
