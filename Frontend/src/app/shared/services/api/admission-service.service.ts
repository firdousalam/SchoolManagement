import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from 'src/app/core/services/api/crud.service';
import { apiRoutes } from '../../constants/api-routes.constant';
import { IAdmission, IAdmissionPage, IAdmissionSearch } from '../../models/admission';
import { UrlBuilderService } from '../url-builder.service';

@Injectable({
  providedIn: 'root'
})
export class AdmissionServiceService extends CrudService<IAdmission, IAdmissionPage, IAdmissionSearch>{

  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService
  ) {
    super(httpClient, urlBuilderService, apiRoutes.admissionApi);
  }
}