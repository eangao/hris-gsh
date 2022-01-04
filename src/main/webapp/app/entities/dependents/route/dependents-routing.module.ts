import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DependentsComponent } from '../list/dependents.component';
import { DependentsDetailComponent } from '../detail/dependents-detail.component';
import { DependentsUpdateComponent } from '../update/dependents-update.component';
import { DependentsRoutingResolveService } from './dependents-routing-resolve.service';

const dependentsRoute: Routes = [
  {
    path: '',
    component: DependentsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DependentsDetailComponent,
    resolve: {
      dependents: DependentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DependentsUpdateComponent,
    resolve: {
      dependents: DependentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DependentsUpdateComponent,
    resolve: {
      dependents: DependentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dependentsRoute)],
  exports: [RouterModule],
})
export class DependentsRoutingModule {}
