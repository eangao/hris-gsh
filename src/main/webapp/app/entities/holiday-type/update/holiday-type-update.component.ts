import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHolidayType, HolidayType } from '../holiday-type.model';
import { HolidayTypeService } from '../service/holiday-type.service';

@Component({
  selector: 'jhi-holiday-type-update',
  templateUrl: './holiday-type-update.component.html',
})
export class HolidayTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected holidayTypeService: HolidayTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holidayType }) => {
      this.updateForm(holidayType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const holidayType = this.createFromForm();
    if (holidayType.id !== undefined) {
      this.subscribeToSaveResponse(this.holidayTypeService.update(holidayType));
    } else {
      this.subscribeToSaveResponse(this.holidayTypeService.create(holidayType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHolidayType>>): void {
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

  protected updateForm(holidayType: IHolidayType): void {
    this.editForm.patchValue({
      id: holidayType.id,
      name: holidayType.name,
    });
  }

  protected createFromForm(): IHolidayType {
    return {
      ...new HolidayType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
