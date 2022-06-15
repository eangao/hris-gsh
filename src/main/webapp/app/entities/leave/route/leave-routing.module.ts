import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveComponent } from '../list/leave.component';
import { LeaveDetailComponent } from '../detail/leave-detail.component';
import { LeaveUpdateComponent } from '../update/leave-update.component';
import { LeaveRoutingResolveService } from './leave-routing-resolve.service';

const leaveRoute: Routes = [
  {
    path: '',
    component: LeaveComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveDetailComponent,
    resolve: {
      leave: LeaveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveUpdateComponent,
    resolve: {
      leave: LeaveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveUpdateComponent,
    resolve: {
      leave: LeaveRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveRoute)],
  exports: [RouterModule],
})
export class LeaveRoutingModule {}
