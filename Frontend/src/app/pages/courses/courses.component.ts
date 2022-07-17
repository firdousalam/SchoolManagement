import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { courseColumnDefs } from 'src/app/shared/constants/course.constant';
import { StudyMaterialServiceService } from 'src/app/shared/services/api/study-material-service.service';
import { ICourse, ICoursePage, ICourseSearch } from 'src/app/shared/models/courses';
import { ITableViewConfig } from 'src/app/shared/models/table-view';
import { DatePipe } from '@angular/common';

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
  docSubscription!: Subscription;
  searchTerm: any = '';
  showPdfUrl: any;
  filename: any;
  constructor(private api: StudyMaterialServiceService,private date:DatePipe) {
    this.adminTableConfig = courseColumnDefs;

  }
  isAction(field: ITableViewConfig, value: any): boolean {
    return value[field.field];
  }
  getRowValue(field: ITableViewConfig, value: any): ICourse {
    return (field.field ==='fromDate' ? value[field.field] ? this.date.transform(value[field.field],'dd/MM/YYYY') : '------------': value[field.field] ? value[field.field] : '------------');
  }
  ngOnInit(): void {
    this.init();
  }

  init() {
    this.courseSubscription = this.api.getBySearchCriteria({}).subscribe((data: ICoursePage) => {
      this.rowData = data.content;
      this.filterData = this.rowData;
    })
  }
  onViewClick(id: any, filename: string) {
    this.docSubscription = this.api.getDoc(id).subscribe((response: any) => {
      let dataType = response.type;
      let binaryData = [];
      binaryData.push(response);
      const pdfUrl: any = window.URL.createObjectURL(new Blob(binaryData, { type: dataType }));
      this.openPopup(pdfUrl, filename);
    })

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
    console.log('ff', downloadLink.href);
    if (this.filename)
      downloadLink.setAttribute('download', this.filename);
    document.body.appendChild(downloadLink);
    downloadLink.click();
    downloadLink?.parentNode?.removeChild(downloadLink);
    this.displayStyle = "none";
  }

  search(value: any): void {
    // console.log(typeof (value), value);
    // this.rowData = this.filterData!.filter((val: any) =>
    //   val.type.toLowerCase().includes(value.trim().toLowerCase()) ||
    //   val.dateofEntry.toLowerCase().includes(value.trim().toLowerCase()) ||
    //   val.queryOrClarification.toLowerCase().includes(value.trim().toLowerCase()) ||
    //   val.response.toLowerCase().includes(value.trim().toLowerCase()) ||
    //   val.queryStatus.toLowerCase().includes(value.trim().toLowerCase())
    // );
  }
}
