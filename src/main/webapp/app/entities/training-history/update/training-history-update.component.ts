import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITrainingHistory, TrainingHistory } from '../training-history.model';
import { TrainingHistoryService } from '../service/training-history.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-training-history-update',
  templateUrl: './training-history-update.component.html',
})
export class TrainingHistoryUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    date: [null, [Validators.required]],
    description: [],
    employee: [null, Validators.required],
  });

  constructor(
    protected trainingHistoryService: TrainingHistoryService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainingHistory }) => {
      this.updateForm(trainingHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trainingHistory = this.createFromForm();
    if (trainingHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.trainingHistoryService.update(trainingHistory));
    } else {
      this.subscribeToSaveResponse(this.trainingHistoryService.create(trainingHistory));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainingHistory>>): void {
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

  protected updateForm(trainingHistory: ITrainingHistory): void {
    this.editForm.patchValue({
      id: trainingHistory.id,
      name: trainingHistory.name,
      date: trainingHistory.date,
      description: trainingHistory.description,
      employee: trainingHistory.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      trainingHistory.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): ITrainingHistory {
    return {
      ...new TrainingHistory(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      date: this.editForm.get(['date'])!.value,
      description: this.editForm.get(['description'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
