import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { courseColumnDefs } from 'src/app/shared/constants/course.constant';
import { StudyMaterialServiceService } from 'src/app/shared/services/api/study-material-service.service';
import { ICourse, ICoursePage, ICourseSearch } from 'src/app/shared/models/courses';
import { ITableViewConfig } from 'src/app/shared/models/table-view';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { CommonService } from 'src/app/shared/services/api/common.service';
import { NzTableQueryParams } from 'ng-zorro-antd/table';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss']
})
export class CoursesComponent implements OnInit {
  adminTableConfig: any[];
  rowData!: ICourse[];
  filterData!: ICourse[];
  courseSubscription!: Subscription;
  routeParamSubscription!: Subscription;
  docSubscription!: Subscription;
  subscriptionArray: any[] = [];
  searchTerm: any = '';
  showPdfUrl: any;
  filename: any;
  studentProfileId: any;
  total = 1;
  loading = false;
  sortOrder = 'DESC';
  pageSize = 10;
  pageIndex = 1;
  constructor(private commonService: CommonService, private route: ActivatedRoute, private api: StudyMaterialServiceService, private date: DatePipe) {
    this.adminTableConfig = courseColumnDefs;

  }
  isAction(field: ITableViewConfig, value: any): boolean {
    return value[field.field];
  }
  getRowValue(field: ITableViewConfig, value: any): ICourse {
    return (field.field === 'fromDate' ? value[field.field] ? this.date.transform(value[field.field], 'dd/MM/YYYY') : '------------' : value[field.field] ? value[field.field] : '------------');
  }
  ngOnInit(): void {
    // this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
    const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
    this.studentProfileId = studentProfileObj.studentProfileId;
    this.commonService.profileSubject.next({ profileId: this.studentProfileId });
    this.init(this.pageIndex, this.pageSize, 'DESC');
    // })
  }
  onQueryParamsChange(params: NzTableQueryParams): void {
    const { pageSize, pageIndex, sort, filter } = params;
    const currentSort = sort.find((item: any) => item.value !== null);
    const sortOrder = (currentSort && currentSort.value) || null;
    this.init(pageIndex, pageSize, sortOrder);
  }
  init(pageIndex: any, pageSize: any, sortOrder: any) {
    this.loading = true;
    pageIndex = pageIndex - 1;
    this.courseSubscription = this.api.getBySearchCriteria({ pageNumber: pageIndex, pageSize: pageSize, sortDirection: this.sortOrder }).subscribe((data: ICoursePage) => {
      this.loading = false;
      this.total = data.totalElements;
      this.pageIndex = data.pageNumber + 1;
      this.rowData = data.content;
      this.filterData = this.rowData;
    })
    this.subscriptionArray.push(this.courseSubscription);
  }
  onViewClick(id: any, filename: string) {
    this.docSubscription = this.api.getDoc(id).subscribe((response: any) => {
      let dataType = response.type;
      let binaryData = [];
      binaryData.push(response);
      const pdfUrl: any = window.URL.createObjectURL(new Blob(binaryData, { type: dataType }));
      this.openPopup(pdfUrl, filename);
    })
    this.subscriptionArray.push(this.docSubscription);

  }

  displayStyle = "none";

  openPopup(src: any, filename: any) {
    this.showPdfUrl = src;
    this.filename = filename;
    this.displayStyle = "block";
  }
  closePopup() {
    this.displayStyle = "none";
  }

  downloadFile(url: string) {
    let downloadLink = document.createElement('a');
    downloadLink.href = url;
    if (this.filename)
      downloadLink.setAttribute('download', this.filename);
    document.body.appendChild(downloadLink);
    downloadLink.click();
    downloadLink?.parentNode?.removeChild(downloadLink);
    this.displayStyle = "none";
  }

  search(value: any): void {
    this.rowData = this.filterData!.filter((val: any) =>
      val?.subject?.toLowerCase()?.includes(value.trim().toLowerCase()) ||
      val?.heading?.toLowerCase()?.includes(value.trim().toLowerCase()) ||
      val?.description?.toLowerCase()?.includes(value.trim().toLowerCase()) ||
      val?.fromDate?.toLowerCase()?.includes(value.trim().toLowerCase())
    );
  }

  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
}
