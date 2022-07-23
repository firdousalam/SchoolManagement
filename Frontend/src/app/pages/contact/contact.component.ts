import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { queryColumnDefs } from 'src/app/shared/constants/query.constant';
import { ContactQueryServiceService } from 'src/app/shared/services/api/contact-query-service.service';
import { GlobalErrorService } from 'src/app/core/services/global-error.service';
import { IContact,IContactSearch,IContactPage,contactSearch } from 'src/app/shared/models/contact';
import { ITableViewConfig } from 'src/app/shared/models/table-view';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  adminTableConfig: ITableViewConfig[];
  rowData!: IContact[];
  filterData!: IContact[];
  subscriptionArray:any[]=[];
  searchTerm = '';
  contactSubscription!: Subscription;
  constructor(private errorServices: GlobalErrorService, private router: Router, private api: ContactQueryServiceService) {
    this.adminTableConfig = queryColumnDefs;

  }

  onTableRowClick(id: any) {
    console.log(id);
    this.router.navigate(['/contact/view'], { queryParams: { id } });
  }
  ngOnInit(): void {
    this.init();
  }

  getRowValue(field: ITableViewConfig,value: any):IContact{
    console.log('hhh',field,value);
    return value[field.field] ? value[field.field]:'------------';
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
