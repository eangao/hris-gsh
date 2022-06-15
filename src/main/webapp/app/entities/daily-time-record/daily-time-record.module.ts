import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DailyTimeRecordComponent } from './list/daily-time-record.component';
import { DailyTimeRecordDetailComponent } from './detail/daily-time-record-detail.component';
import { DailyTimeRecordUpdateComponent } from './update/daily-time-record-update.component';
import { DailyTimeRecordDeleteDialogComponent } from './delete/daily-time-record-delete-dialog.component';
import { DailyTimeRecordRoutingModule } from './route/daily-time-record-routing.module';

@NgModule({
  imports: [SharedModule, DailyTimeRecordRoutingModule],
  declarations: [
    DailyTimeRecordComponent,
    DailyTimeRecordDetailComponent,
    DailyTimeRecordUpdateComponent,
    DailyTimeRecordDeleteDialogComponent,
  ],
  entryComponents: [DailyTimeRecordDeleteDialogComponent],
})
export class DailyTimeRecordModule {}
