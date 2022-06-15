import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClusterComponent } from '../list/cluster.component';
import { ClusterDetailComponent } from '../detail/cluster-detail.component';
import { ClusterUpdateComponent } from '../update/cluster-update.component';
import { ClusterRoutingResolveService } from './cluster-routing-resolve.service';

const clusterRoute: Routes = [
  {
    path: '',
    component: ClusterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClusterDetailComponent,
    resolve: {
      cluster: ClusterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClusterUpdateComponent,
    resolve: {
      cluster: ClusterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClusterUpdateComponent,
    resolve: {
      cluster: ClusterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(clusterRoute)],
  exports: [RouterModule],
})
export class ClusterRoutingModule {}
