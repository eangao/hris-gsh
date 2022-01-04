import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBenefits, Benefits } from '../benefits.model';
import { BenefitsService } from '../service/benefits.service';

@Component({
  selector: 'jhi-benefits-update',
  templateUrl: './benefits-update.component.html',
})
export class BenefitsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected benefitsService: BenefitsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ benefits }) => {
      this.updateForm(benefits);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const benefits = this.createFromForm();
    if (benefits.id !== undefined) {
      this.subscribeToSaveResponse(this.benefitsService.update(benefits));
    } else {
      this.subscribeToSaveResponse(this.benefitsService.create(benefits));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBenefits>>): void {
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

  protected updateForm(benefits: IBenefits): void {
    this.editForm.patchValue({
      id: benefits.id,
      name: benefits.name,
    });
  }

  protected createFromForm(): IBenefits {
    return {
      ...new Benefits(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
