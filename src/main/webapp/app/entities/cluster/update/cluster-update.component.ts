import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICluster, Cluster } from '../cluster.model';
import { ClusterService } from '../service/cluster.service';

@Component({
  selector: 'jhi-cluster-update',
  templateUrl: './cluster-update.component.html',
})
export class ClusterUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected clusterService: ClusterService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cluster }) => {
      this.updateForm(cluster);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cluster = this.createFromForm();
    if (cluster.id !== undefined) {
      this.subscribeToSaveResponse(this.clusterService.update(cluster));
    } else {
      this.subscribeToSaveResponse(this.clusterService.create(cluster));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICluster>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cluster: ICluster): void {
    this.editForm.patchValue({
      id: cluster.id,
      name: cluster.name,
    });
  }

  protected createFromForm(): ICluster {
    return {
      ...new Cluster(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
