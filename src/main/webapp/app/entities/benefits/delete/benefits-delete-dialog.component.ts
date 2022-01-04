import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBenefits } from '../benefits.model';
import { BenefitsService } from '../service/benefits.service';

@Component({
  templateUrl: './benefits-delete-dialog.component.html',
})
export class BenefitsDeleteDialogComponent {
  benefits?: IBenefits;

  constructor(protected benefitsService: BenefitsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.benefitsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
