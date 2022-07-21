import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UrlBuilderService } from 'src/app/shared/services/url-builder.service';

@Injectable({
  providedIn: 'root',
})
export abstract class CrudService<IDetail, IDetailPage, ISearch> {
  constructor(
    protected httpClient: HttpClient,
    protected urlBuilderService: UrlBuilderService,
    @Inject('api') protected api: any
  ) {}

  getAll() {
    return this.httpClient.get<IDetail[]>(
      this.urlBuilderService.getUrl(this.api.getAll)
    );
  }

  getByID(searchObject: ISearch) {
    return this.httpClient.get<IDetailPage>(
       this.urlBuilderService.getUrlWithQueryParams(
        this.api.getById,
        searchObject
      )
    );
  }

  getByIDWithDetail(searchObject: ISearch) {
    return this.httpClient.get<IDetail>(
       this.urlBuilderService.getUrlWithQueryParams(
        this.api.getByIDWithDetail,
        searchObject
      )
    );
  }
  getDoc(id: number) {
    return this.httpClient.get(
       this.urlBuilderService.getUrl(this.api.getDoc+'/'+id),{responseType: 'blob' as 'json'}
    );
  }

  getBySearchCriteria(searchObject: ISearch) {
    return this.httpClient.get<IDetailPage>(
      this.urlBuilderService.getUrlWithQueryParams(
        this.api.search,
        searchObject
      )
    );
  }

  update(detail: IDetail) {
    return this.httpClient.put(
      this.urlBuilderService.getUrl(this.api.update),
      detail
    );
  }
  updateBySearchCriteria(searchObject: ISearch,detail: IDetail) {    
    return this.httpClient.put(
      this.urlBuilderService.getUrlWithQueryParams(
        this.api.update,
        searchObject
      ),
      detail
    );
  }
  updateBySearchCriteriaWithArrayObject(searchObject: ISearch,detail: IDetail[]) {
    return this.httpClient.put(
      this.urlBuilderService.getUrlWithQueryParams(
        this.api.update,
        searchObject
      ),
      detail
    );
  }

  create(detail: IDetail) {
    return this.httpClient.post(
      this.urlBuilderService.getUrl(this.api.create),
      detail
    );
  }
}
