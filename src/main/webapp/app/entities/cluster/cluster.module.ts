import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClusterComponent } from './list/cluster.component';
import { ClusterDetailComponent } from './detail/cluster-detail.component';
import { ClusterUpdateComponent } from './update/cluster-update.component';
import { ClusterDeleteDialogComponent } from './delete/cluster-delete-dialog.component';
import { ClusterRoutingModule } from './route/cluster-routing.module';

@NgModule({
  imports: [SharedModule, ClusterRoutingModule],
  declarations: [ClusterComponent, ClusterDetailComponent, ClusterUpdateComponent, ClusterDeleteDialogComponent],
  entryComponents: [ClusterDeleteDialogComponent],
})
export class ClusterModule {}
