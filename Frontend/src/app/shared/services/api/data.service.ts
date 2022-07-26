import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IType, ITypePage, ITypeSearch } from '../../models/type';
import { apiRoutes } from '../../constants/api-routes.constant';
import { UrlBuilderService } from '../url-builder.service';
import { CrudService } from 'src/app/core/services/api/crud.service';

@Injectable({
  providedIn: 'root',
})
export class DataService extends CrudService<IType, ITypePage, ITypeSearch> {
  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService
  ) {
    super(httpClient, urlBuilderService, apiRoutes.dataApi);
  }
}
