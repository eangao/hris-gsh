import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEducation } from '../education.model';
import { EducationService } from '../service/education.service';

@Component({
  templateUrl: './education-delete-dialog.component.html',
})
export class EducationDeleteDialogComponent {
  education?: IEducation;

  constructor(protected educationService: EducationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.educationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
