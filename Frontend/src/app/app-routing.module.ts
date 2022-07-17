import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RouteGuard } from './core/guards/route.guard';
import { AdmissionComponent } from './pages/admission/admission.component';
import { AddContactComponent } from './pages/contact/add-contact/add-contact.component';
import { ContactComponent } from './pages/contact/contact.component';
import { ViewContactComponent } from './pages/contact/view-contact/view-contact.component';
import { CoursesComponent } from './pages/courses/courses.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { StudentProfileDetailComponent } from './pages/student-profile-detail/student-profile-detail.component';

const routes: Routes = [
  { path: 'dashboard/:id', component: DashboardComponent, canActivate:[RouteGuard] },
  { path: 'admission', component: AdmissionComponent },
  { path: 'courses', component: CoursesComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'contact/new', component: AddContactComponent },
  { path: 'contact/view', component: ViewContactComponent },
  { path: 'student/profile/:id', component: StudentProfileDetailComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
