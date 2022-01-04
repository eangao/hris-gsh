import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HolidayTypeComponent } from '../list/holiday-type.component';
import { HolidayTypeDetailComponent } from '../detail/holiday-type-detail.component';
import { HolidayTypeUpdateComponent } from '../update/holiday-type-update.component';
import { HolidayTypeRoutingResolveService } from './holiday-type-routing-resolve.service';

const holidayTypeRoute: Routes = [
  {
    path: '',
    component: HolidayTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HolidayTypeDetailComponent,
    resolve: {
      holidayType: HolidayTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HolidayTypeUpdateComponent,
    resolve: {
      holidayType: HolidayTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HolidayTypeUpdateComponent,
    resolve: {
      holidayType: HolidayTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(holidayTypeRoute)],
  exports: [RouterModule],
})
export class HolidayTypeRoutingModule {}
