import { Injectable } from '@angular/core';
import { ConfigurationService } from '../../core/services/configuration.service';

@Injectable({
  providedIn: 'root',
})
export class UrlBuilderService {
  protected apiServer = ConfigurationService.settings.apiServer;

  constructor() {}

  getUrl(restOfUrl: string) {
    return `${this.apiServer.baseUrl}${restOfUrl}`;
  }

  getUrlWithQueryParams(restOfUrl: string, queryParams: any) {
    const strippedQueryParams = { ...queryParams };
    Object.keys(strippedQueryParams).forEach(key => {
      if (
        strippedQueryParams[key] === null ||
        strippedQueryParams[key]?.toString() === ''
      ) {
        delete strippedQueryParams[key];
      }
    });
    const params = new URLSearchParams(strippedQueryParams);    
    return `${this.apiServer.baseUrl}${restOfUrl}?${params.toString()}`;
  }

  
}
