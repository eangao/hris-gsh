import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BenefitsComponent } from '../list/benefits.component';
import { BenefitsDetailComponent } from '../detail/benefits-detail.component';
import { BenefitsUpdateComponent } from '../update/benefits-update.component';
import { BenefitsRoutingResolveService } from './benefits-routing-resolve.service';

const benefitsRoute: Routes = [
  {
    path: '',
    component: BenefitsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BenefitsDetailComponent,
    resolve: {
      benefits: BenefitsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BenefitsUpdateComponent,
    resolve: {
      benefits: BenefitsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BenefitsUpdateComponent,
    resolve: {
      benefits: BenefitsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(benefitsRoute)],
  exports: [RouterModule],
})
export class BenefitsRoutingModule {}
