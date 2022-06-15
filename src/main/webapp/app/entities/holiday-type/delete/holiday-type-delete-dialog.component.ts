import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHolidayType } from '../holiday-type.model';
import { HolidayTypeService } from '../service/holiday-type.service';

@Component({
  templateUrl: './holiday-type-delete-dialog.component.html',
})
export class HolidayTypeDeleteDialogComponent {
  holidayType?: IHolidayType;

  constructor(protected holidayTypeService: HolidayTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.holidayTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
