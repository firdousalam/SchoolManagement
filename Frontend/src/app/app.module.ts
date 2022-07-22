import { NgModule, APP_INITIALIZER, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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
import { NzFormModule } from 'ng-zorro-antd/form';
import { SpinnerComponent } from './core/components/spinner/spinner.component';
import { HttpRequestInterceptor } from './core/interceptors/http-request.interceptor';
import { TableViewComponent } from './shared/components/table-view/table-view.component';
import { NZ_I18N, en_US } from 'ng-zorro-antd/i18n';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzModalModule  } from 'ng-zorro-antd/modal';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzCheckboxModule } from 'ng-zorro-antd/checkbox';
import { NZ_ICONS } from 'ng-zorro-antd/icon';
import { IconDefinition } from '@ant-design/icons-angular';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import * as AllIcons from '@ant-design/icons-angular/icons';
import { AdmissionComponent } from './pages/admission/admission.component';
import { CoursesComponent } from './pages/courses/courses.component';
import { ContactComponent } from './pages/contact/contact.component';
import { AddContactComponent } from './pages/contact/add-contact/add-contact.component';
import { ViewContactComponent } from './pages/contact/view-contact/view-contact.component';
import { FeePaymentComponent } from './pages/admission/fee-payment/fee-payment.component';
import { PaidPaymentComponent } from './pages/admission/paid-payment/paid-payment.component';
import { AdmissionDetailsComponent } from './pages/student-profile-detail/sections/admission-details/admission-details.component';
import { EducationalDetailsComponent } from './pages/student-profile-detail/sections/educational-details/educational-details.component';
import { EmploymentDetailsComponent } from './pages/student-profile-detail/sections/employment-details/employment-details.component';
import { PersonalDetailsComponent } from './pages/student-profile-detail/sections/personal-details/personal-details.component';
import { PersonalHeaderDetailsComponent } from './pages/student-profile-detail/sections/personal-header-details/personal-header-details.component';
import { StudentProfileDetailComponent } from './pages/student-profile-detail/student-profile-detail.component';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { RouteGuard } from './core/guards/route.guard';
import { SafePipePipe } from './shared/safe-pipe.pipe';
import { DatePipe } from '@angular/common';
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
    AdmissionComponent,
    CoursesComponent,
    ContactComponent,
    AddContactComponent,
    ViewContactComponent,
    FeePaymentComponent,
    PaidPaymentComponent,
    StudentProfileDetailComponent,
    AdmissionDetailsComponent,
    EducationalDetailsComponent,
    EmploymentDetailsComponent,
    PersonalDetailsComponent,
    PersonalHeaderDetailsComponent,
    PageNotFoundComponent,
    SafePipePipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule,
    AgGridModule,
    NzTableModule,
    NzMenuModule,
    NzToolTipModule,
    NzLayoutModule,
    NzIconModule,
    NzModalModule,
    NzCheckboxModule,
    NzPopconfirmModule,
    NzSelectModule,
    NzFormModule,
    NzTabsModule,
    BrowserAnimationsModule
  ],
  providers: [
    RouteGuard,DatePipe,
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
