import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDutySchedule } from '../duty-schedule.model';
import { DutyScheduleService } from '../service/duty-schedule.service';

@Component({
  templateUrl: './duty-schedule-delete-dialog.component.html',
})
export class DutyScheduleDeleteDialogComponent {
  dutySchedule?: IDutySchedule;

  constructor(protected dutyScheduleService: DutyScheduleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dutyScheduleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
