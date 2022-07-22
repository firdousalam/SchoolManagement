import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from 'src/app/core/services/api/crud.service';
import { apiRoutes } from '../../constants/api-routes.constant';
import { stdPersonalDetail,IProfilePage,IProfileSearch } from '../../models/profile';
import { UrlBuilderService } from '../url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileHeaderService extends CrudService<stdPersonalDetail, IProfilePage, IProfileSearch>{

  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService
  ) {
    super(httpClient, urlBuilderService, apiRoutes.studentPersonalApi);
  }
}
