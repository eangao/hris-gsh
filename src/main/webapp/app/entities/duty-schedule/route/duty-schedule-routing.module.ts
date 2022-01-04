import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DutyScheduleComponent } from '../list/duty-schedule.component';
import { DutyScheduleDetailComponent } from '../detail/duty-schedule-detail.component';
import { DutyScheduleUpdateComponent } from '../update/duty-schedule-update.component';
import { DutyScheduleRoutingResolveService } from './duty-schedule-routing-resolve.service';

const dutyScheduleRoute: Routes = [
  {
    path: '',
    component: DutyScheduleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DutyScheduleDetailComponent,
    resolve: {
      dutySchedule: DutyScheduleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DutyScheduleUpdateComponent,
    resolve: {
      dutySchedule: DutyScheduleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DutyScheduleUpdateComponent,
    resolve: {
      dutySchedule: DutyScheduleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dutyScheduleRoute)],
  exports: [RouterModule],
})
export class DutyScheduleRoutingModule {}
