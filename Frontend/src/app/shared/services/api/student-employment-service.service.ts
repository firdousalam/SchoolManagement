import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from 'src/app/core/services/api/crud.service';
import { apiRoutes } from '../../constants/api-routes.constant';
import { IProfile, IProfilePage, IProfileSearch, stdProfession } from '../../models/profile';
import { UrlBuilderService } from '../url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class StudentEmploymentServiceService extends CrudService<stdProfession,IProfilePage,IProfileSearch>{

  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService
  ) {
    super(httpClient, urlBuilderService, apiRoutes.studentEmploymentApi);
  }
}