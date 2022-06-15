import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDailyTimeRecord } from '../daily-time-record.model';
import { DailyTimeRecordService } from '../service/daily-time-record.service';

@Component({
  templateUrl: './daily-time-record-delete-dialog.component.html',
})
export class DailyTimeRecordDeleteDialogComponent {
  dailyTimeRecord?: IDailyTimeRecord;

  constructor(protected dailyTimeRecordService: DailyTimeRecordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dailyTimeRecordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
