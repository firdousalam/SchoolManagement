import { Component, Input, Output, EventEmitter } from '@angular/core';
import { GlobalDataService } from 'src/app/core/services/global-data.service';
import { Router } from '@angular/router';
import { ITableViewConfig } from '../../models/table-view';
import { IPagination } from '../../models/pagination';
import { defaultPageSize } from '../../constants/common.constant';
import { NzTableQueryParams } from 'ng-zorro-antd/table/ng-zorro-antd-table';

@Component({
  selector: 'app-table-view',
  templateUrl: './table-view.component.html',
  styleUrls: ['./table-view.component.scss'],
})
export class TableViewComponent {
  @Input() adminTableConfig: ITableViewConfig[] = [];
  @Input() rowData: any = [];
  @Input() selectedTab: string = '';
  @Input() totalElements: number = 0;
  @Output() pageChange = new EventEmitter<IPagination>();

  pageNumber: number = 1;
  pageSize: number = defaultPageSize;

  constructor(
    private globalDataService: GlobalDataService,
    private router: Router
  ) {}

  // onRowEdit(detail: IBatch) {
  onRowEdit(detail: any) {
    // this.globalDataService.updateAdminDetail(detail);
    // this.router.navigateByUrl(
    //   `${configTabDetailRoute[this.selectedTab]}/${detail.id}`
    // );
  }

  onPageChange(params: NzTableQueryParams) {
    const { pageSize, pageIndex } = params;
    const pageNumber =
      typeof pageIndex === 'number' && pageIndex > 0 ? pageIndex - 1 : 0;
    this.pageChange.emit({ pageNumber, pageSize });
  }
}
