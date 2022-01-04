import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDesignation, Designation } from '../designation.model';
import { DesignationService } from '../service/designation.service';

@Component({
  selector: 'jhi-designation-update',
  templateUrl: './designation-update.component.html',
})
export class DesignationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    description: [],
  });

  constructor(protected designationService: DesignationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ designation }) => {
      this.updateForm(designation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const designation = this.createFromForm();
    if (designation.id !== undefined) {
      this.subscribeToSaveResponse(this.designationService.update(designation));
    } else {
      this.subscribeToSaveResponse(this.designationService.create(designation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDesignation>>): void {
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

  protected updateForm(designation: IDesignation): void {
    this.editForm.patchValue({
      id: designation.id,
      name: designation.name,
      description: designation.description,
    });
  }

  protected createFromForm(): IDesignation {
    return {
      ...new Designation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
