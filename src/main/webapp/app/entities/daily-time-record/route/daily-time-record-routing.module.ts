import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DailyTimeRecordComponent } from '../list/daily-time-record.component';
import { DailyTimeRecordDetailComponent } from '../detail/daily-time-record-detail.component';
import { DailyTimeRecordUpdateComponent } from '../update/daily-time-record-update.component';
import { DailyTimeRecordRoutingResolveService } from './daily-time-record-routing-resolve.service';

const dailyTimeRecordRoute: Routes = [
  {
    path: '',
    component: DailyTimeRecordComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DailyTimeRecordDetailComponent,
    resolve: {
      dailyTimeRecord: DailyTimeRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DailyTimeRecordUpdateComponent,
    resolve: {
      dailyTimeRecord: DailyTimeRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DailyTimeRecordUpdateComponent,
    resolve: {
      dailyTimeRecord: DailyTimeRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dailyTimeRecordRoute)],
  exports: [RouterModule],
})
export class DailyTimeRecordRoutingModule {}
