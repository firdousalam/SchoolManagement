import { NgModule, APP_INITIALIZER, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ConfigurationService } from './core/services/configuration.service';
import { SideNavComponent } from './core/components/side-nav/side-nav.component';
import { MainHeaderComponent } from './core/components/main-header/main-header.component';
import { HeadingComponent } from './shared/components/heading/heading.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HeaderTabsComponent } from './shared/components/header-tabs/header-tabs.component';
import { AgGridModule } from 'ag-grid-angular';
import { NotificationStackComponent } from './core/components/notifications/notification-stack/notification-stack.component';
import { NotificationComponent } from './core/components/notifications/notification/notification.component';
import { ApiErrorInterceptor } from './core/interceptors/api-error.interceptor';
import { GlobalErrorHandler } from './core/interceptors/global-error-handler';
import { NzTableModule } from 'ng-zorro-antd/table';
import { SpinnerComponent } from './core/components/spinner/spinner.component';
import { HttpRequestInterceptor } from './core/interceptors/http-request.interceptor';
import { TableViewComponent } from './shared/components/table-view/table-view.component';
import { NZ_I18N, en_US } from 'ng-zorro-antd/i18n';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NZ_ICONS } from 'ng-zorro-antd/icon';
import { IconDefinition } from '@ant-design/icons-angular';
import * as AllIcons from '@ant-design/icons-angular/icons';

export function initializeApp(configurationService: ConfigurationService) {
  return (): Promise<any> => {
    return configurationService.load();
  };
}

const antDesignIcons = AllIcons as {
  [key: string]: IconDefinition;
};
const icons: IconDefinition[] = Object.keys(antDesignIcons).map(
  key => antDesignIcons[key]
);

@NgModule({
  declarations: [
    AppComponent,
    MainHeaderComponent,
    SideNavComponent,
    HeadingComponent,
    DashboardComponent,
    HeaderTabsComponent,
    NotificationComponent,
    NotificationStackComponent,
    SpinnerComponent,
    TableViewComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    AgGridModule,
    NzTableModule,
    NzMenuModule,
    NzToolTipModule,
    NzLayoutModule,
    NzIconModule,
    BrowserAnimationsModule,
  ],
  providers: [
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiErrorInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true,
    },
    ConfigurationService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [ConfigurationService],
      multi: true,
    },
    { provide: NZ_I18N, useValue: en_US },
    { provide: NZ_ICONS, useValue: icons },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
