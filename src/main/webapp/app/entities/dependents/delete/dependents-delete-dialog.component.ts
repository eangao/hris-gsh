import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDependents } from '../dependents.model';
import { DependentsService } from '../service/dependents.service';

@Component({
  templateUrl: './dependents-delete-dialog.component.html',
})
export class DependentsDeleteDialogComponent {
  dependents?: IDependents;

  constructor(protected dependentsService: DependentsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dependentsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
