import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IConfiguration } from '../models/configuration';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {
  static settings: IConfiguration;

  constructor(private http: HttpClient) {}
  load() {
    const configFile = `assets/configuration/config.json`;

    return new Promise<void>((resolve, reject) => {
      this.http
        .get(configFile)
        .toPromise()
        .then(response => {
          ConfigurationService.settings = <IConfiguration>response;
          resolve();
        })
        .catch((response: any) => {
          reject(`Could not load the config file`);
        });
    });
  }
}
