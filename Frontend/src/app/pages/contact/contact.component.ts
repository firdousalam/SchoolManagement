import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { queryColumnDefs } from 'src/app/shared/constants/query.constant';
import { ContactQueryServiceService } from 'src/app/shared/services/api/contact-query-service.service';
import { GlobalErrorService } from 'src/app/core/services/global-error.service';
import { IContact, IContactSearch, IContactPage, contactSearch } from 'src/app/shared/models/contact';
import { ITableViewConfig } from 'src/app/shared/models/table-view';
import { CommonService } from 'src/app/shared/services/api/common.service';
import * as moment from 'moment';
import { NzTableQueryParams } from 'ng-zorro-antd/table';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  adminTableConfig: ITableViewConfig[];
  rowData!: IContact[];
  filterData!: IContact[];
  subscriptionArray: any[] = [];
  searchTerm = '';
  total = 10;
  loading = false;
  pageSize = 10;
  pageIndex = 1;
  contactSubscription!: Subscription;
  routeParamSubscription!: Subscription;
  studentProfileId: any;
  constructor(private route: ActivatedRoute, private commonService: CommonService, private errorServices: GlobalErrorService, private router: Router, private api: ContactQueryServiceService) {
    this.adminTableConfig = queryColumnDefs;

  }

  onTableRowClick(id: any) {
    console.log(id);
    this.router.navigate([`/contact/${this.studentProfileId}/view`], { queryParams: { id } });
  }
  ngOnInit(): void {
    this.routeParamSubscription = this.route.paramMap.subscribe((params: ParamMap) => {
      console.log(params);

      this.studentProfileId = params.get('id');
      this.commonService.profileSubject.next({ profileId: this.studentProfileId });
      this.init();
    })
  }
  onQueryParamsChange(params: NzTableQueryParams): void {
    console.log(params);
    const { pageSize, pageIndex, sort, filter } = params;
    const currentSort = sort.find((item: any) => item.value !== null);
    const sortOrder = (currentSort && currentSort.value) || null;
    //this.loadDataFromServer(pageIndex, pageSize, sortField, sortOrder, filter);
  }
  getRowValue(field: ITableViewConfig, value: any): IContact {
    if (field.field === 'dateofEntry' && value[field.field] && moment(value[field.field], 'MM/DD/YYYY').format('MM/DD/YYYY') === value[field.field]) {

      value.dateofEntry = moment(value[field.field]).format('DD/MM/YYYY');
    }
    return value[field.field] ? value[field.field] : '------------';
  }
  init() {
    this.contactSubscription = this.api.getBySearchCriteria({}).subscribe((data: IContactPage) => {
      console.log(data);
      this.rowData = data.content;
      this.filterData = this.rowData;
    })
    this.subscriptionArray.push(this.contactSubscription);
  }
  search(value: any): void {
    console.log(typeof (value), value);
    this.rowData = this.filterData!.filter((val: any) =>
      val?.type?.toLowerCase().includes(value.trim().toLowerCase()) ||
      val.dateofEntry.toLowerCase().includes(value.trim().toLowerCase()) ||
      val.queryOrClarification.toLowerCase().includes(value.trim().toLowerCase()) ||
      val.response.toLowerCase().includes(value.trim().toLowerCase()) ||
      val.queryStatus.toLowerCase().includes(value.trim().toLowerCase())
    );
  }
  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
}
