import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from 'src/app/core/services/api/crud.service';
import { apiRoutes } from '../../constants/api-routes.constant';
import { IContact,IContactSearch,IContactPage } from '../../models/contact';
import { UrlBuilderService } from '../url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class ContactQueryServiceService extends CrudService<IContact, IContactPage, IContactSearch>{

  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService
  ) {
    super(httpClient, urlBuilderService, apiRoutes.queryOrContactApi);
  }
}
