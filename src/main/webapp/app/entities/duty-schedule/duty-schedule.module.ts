import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DutyScheduleComponent } from './list/duty-schedule.component';
import { DutyScheduleDetailComponent } from './detail/duty-schedule-detail.component';
import { DutyScheduleUpdateComponent } from './update/duty-schedule-update.component';
import { DutyScheduleDeleteDialogComponent } from './delete/duty-schedule-delete-dialog.component';
import { DutyScheduleRoutingModule } from './route/duty-schedule-routing.module';

@NgModule({
  imports: [SharedModule, DutyScheduleRoutingModule],
  declarations: [DutyScheduleComponent, DutyScheduleDetailComponent, DutyScheduleUpdateComponent, DutyScheduleDeleteDialogComponent],
  entryComponents: [DutyScheduleDeleteDialogComponent],
})
export class DutyScheduleModule {}
