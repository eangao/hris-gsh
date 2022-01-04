import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDepartment, Department } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { ICluster } from 'app/entities/cluster/cluster.model';
import { ClusterService } from 'app/entities/cluster/service/cluster.service';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;

  clustersSharedCollection: ICluster[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    cluster: [null, Validators.required],
  });

  constructor(
    protected departmentService: DepartmentService,
    protected clusterService: ClusterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.updateForm(department);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.createFromForm();
    if (department.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  trackClusterById(index: number, item: ICluster): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
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

  protected updateForm(department: IDepartment): void {
    this.editForm.patchValue({
      id: department.id,
      name: department.name,
      cluster: department.cluster,
    });

    this.clustersSharedCollection = this.clusterService.addClusterToCollectionIfMissing(this.clustersSharedCollection, department.cluster);
  }

  protected loadRelationshipsOptions(): void {
    this.clusterService
      .query()
      .pipe(map((res: HttpResponse<ICluster[]>) => res.body ?? []))
      .pipe(
        map((clusters: ICluster[]) => this.clusterService.addClusterToCollectionIfMissing(clusters, this.editForm.get('cluster')!.value))
      )
      .subscribe((clusters: ICluster[]) => (this.clustersSharedCollection = clusters));
  }

  protected createFromForm(): IDepartment {
    return {
      ...new Department(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      cluster: this.editForm.get(['cluster'])!.value,
    };
  }
}
