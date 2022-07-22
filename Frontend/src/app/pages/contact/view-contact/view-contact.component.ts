import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ContactQueryServiceService } from 'src/app/shared/services/api/contact-query-service.service';
import { IContactSearch,IContactPage } from 'src/app/shared/models/contact';
@Component({
  selector: 'app-view-contact',
  templateUrl: './view-contact.component.html',
  styleUrls: ['./view-contact.component.scss']
})
export class ViewContactComponent implements OnInit,AfterViewInit {
  routeParamSubscription: Subscription | undefined;
  getContactSubscription: Subscription | undefined;
  subscriptionArray:any[]=[];
  contactData:any;
  constructor(private router:Router,private route:ActivatedRoute,private contactService:ContactQueryServiceService) { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.routeParamSubscription = this.route.queryParams.subscribe((params:Params)=>{
      const id = params['id'];
      this.loadData(id);
    })
    this.subscriptionArray.push(this.routeParamSubscription)
  }

  loadData(id:number){
    const params:IContactSearch = {id};
    this.getContactSubscription = this.contactService.getByID(params).subscribe((data: IContactPage)=>{
      this.contactData = data;
    })
    this.subscriptionArray.push(this.getContactSubscription)
  }
  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
}
