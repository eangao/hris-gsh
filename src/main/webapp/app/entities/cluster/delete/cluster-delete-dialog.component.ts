import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICluster } from '../cluster.model';
import { ClusterService } from '../service/cluster.service';

@Component({
  templateUrl: './cluster-delete-dialog.component.html',
})
export class ClusterDeleteDialogComponent {
  cluster?: ICluster;

  constructor(protected clusterService: ClusterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clusterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
