import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrainingHistoryComponent } from './list/training-history.component';
import { TrainingHistoryDetailComponent } from './detail/training-history-detail.component';
import { TrainingHistoryUpdateComponent } from './update/training-history-update.component';
import { TrainingHistoryDeleteDialogComponent } from './delete/training-history-delete-dialog.component';
import { TrainingHistoryRoutingModule } from './route/training-history-routing.module';

@NgModule({
  imports: [SharedModule, TrainingHistoryRoutingModule],
  declarations: [
    TrainingHistoryComponent,
    TrainingHistoryDetailComponent,
    TrainingHistoryUpdateComponent,
    TrainingHistoryDeleteDialogComponent,
  ],
  entryComponents: [TrainingHistoryDeleteDialogComponent],
})
export class TrainingHistoryModule {}
