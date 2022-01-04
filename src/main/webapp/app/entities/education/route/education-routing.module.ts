import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EducationComponent } from '../list/education.component';
import { EducationDetailComponent } from '../detail/education-detail.component';
import { EducationUpdateComponent } from '../update/education-update.component';
import { EducationRoutingResolveService } from './education-routing-resolve.service';

const educationRoute: Routes = [
  {
    path: '',
    component: EducationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EducationDetailComponent,
    resolve: {
      education: EducationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EducationUpdateComponent,
    resolve: {
      education: EducationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EducationUpdateComponent,
    resolve: {
      education: EducationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(educationRoute)],
  exports: [RouterModule],
})
export class EducationRoutingModule {}
