import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DependentsComponent } from './list/dependents.component';
import { DependentsDetailComponent } from './detail/dependents-detail.component';
import { DependentsUpdateComponent } from './update/dependents-update.component';
import { DependentsDeleteDialogComponent } from './delete/dependents-delete-dialog.component';
import { DependentsRoutingModule } from './route/dependents-routing.module';

@NgModule({
  imports: [SharedModule, DependentsRoutingModule],
  declarations: [DependentsComponent, DependentsDetailComponent, DependentsUpdateComponent, DependentsDeleteDialogComponent],
  entryComponents: [DependentsDeleteDialogComponent],
})
export class DependentsModule {}
