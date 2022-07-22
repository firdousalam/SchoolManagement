import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { CrudService } from 'src/app/core/services/api/crud.service';
import { apiRoutes } from '../../constants/api-routes.constant';
import {  IProfile, IProfilePage, IProfileSearch } from '../../models/profile';
import { UrlBuilderService } from '../url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class StudentProfileServiceService extends CrudService<IProfile, IProfilePage, IProfileSearch>{
  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService
  ) {
    super(httpClient, urlBuilderService, apiRoutes.studentProfileApi);
  }
}
