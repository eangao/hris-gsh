import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaveComponent } from './list/leave.component';
import { LeaveDetailComponent } from './detail/leave-detail.component';
import { LeaveUpdateComponent } from './update/leave-update.component';
import { LeaveDeleteDialogComponent } from './delete/leave-delete-dialog.component';
import { LeaveRoutingModule } from './route/leave-routing.module';

@NgModule({
  imports: [SharedModule, LeaveRoutingModule],
  declarations: [LeaveComponent, LeaveDetailComponent, LeaveUpdateComponent, LeaveDeleteDialogComponent],
  entryComponents: [LeaveDeleteDialogComponent],
})
export class LeaveModule {}
