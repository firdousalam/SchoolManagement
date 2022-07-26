import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ContactQueryServiceService } from 'src/app/shared/services/api/contact-query-service.service';
import { IContactSearch,IContactPage } from 'src/app/shared/models/contact';
import { CommonService } from 'src/app/shared/services/api/common.service';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-view-contact',
  templateUrl: './view-contact.component.html',
  styleUrls: ['./view-contact.component.scss']
})
export class ViewContactComponent implements OnInit,AfterViewInit {
  routeParamSubscription: Subscription | undefined;
  routeQueryParamSubscription: Subscription | undefined;
  getContactSubscription: Subscription | undefined;
  subscriptionArray:any[]=[];
  contactData:any;
  studentProfileId:any;
  constructor(private commonService:CommonService,private router:Router,private route:ActivatedRoute,private contactService:ContactQueryServiceService,private datePipe:DatePipe) { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
   // this.routeParamSubscription = this.route.paramMap.subscribe((params:ParamMap)=>{
    const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
    this.studentProfileId = studentProfileObj.studentProfileId;
      this.commonService.profileSubject.next({profileId:this.studentProfileId});
   // })
    this.routeQueryParamSubscription = this.route.queryParams.subscribe((params:Params)=>{
      const id = params['id'];
      this.loadData(id);
    })
   // this.subscriptionArray.push(this.routeParamSubscription)
    this.subscriptionArray.push(this.routeQueryParamSubscription)
  }

  loadData(id:number){
    const params:IContactSearch = {id};
    this.getContactSubscription = this.contactService.getByID(params).subscribe((data: IContactPage)=>{
      this.contactData = data;
    })
    this.subscriptionArray.push(this.getContactSubscription)
  }

  getDate(){
    return moment(this.contactData?.dateofEntry, 'MM/DD/YYYY').format('MM/DD/YYYY') === this.contactData?.dateofEntry ? (this.datePipe.transform(this.contactData?.dateofEntry)): this.contactData?.dateofEntry;
  }
  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
}
