import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeave } from '../leave.model';
import { LeaveService } from '../service/leave.service';

@Component({
  templateUrl: './leave-delete-dialog.component.html',
})
export class LeaveDeleteDialogComponent {
  leave?: ILeave;

  constructor(protected leaveService: LeaveService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
