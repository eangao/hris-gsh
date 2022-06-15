import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDutySchedule, DutySchedule } from '../duty-schedule.model';
import { DutyScheduleService } from '../service/duty-schedule.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-duty-schedule-update',
  templateUrl: './duty-schedule-update.component.html',
})
export class DutyScheduleUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    dateTimeIn: [],
    dateTimeOut: [],
    employee: [null, Validators.required],
  });

  constructor(
    protected dutyScheduleService: DutyScheduleService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dutySchedule }) => {
      if (dutySchedule.id === undefined) {
        const today = dayjs().startOf('day');
        dutySchedule.dateTimeIn = today;
        dutySchedule.dateTimeOut = today;
      }

      this.updateForm(dutySchedule);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dutySchedule = this.createFromForm();
    if (dutySchedule.id !== undefined) {
      this.subscribeToSaveResponse(this.dutyScheduleService.update(dutySchedule));
    } else {
      this.subscribeToSaveResponse(this.dutyScheduleService.create(dutySchedule));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDutySchedule>>): void {
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

  protected updateForm(dutySchedule: IDutySchedule): void {
    this.editForm.patchValue({
      id: dutySchedule.id,
      dateTimeIn: dutySchedule.dateTimeIn ? dutySchedule.dateTimeIn.format(DATE_TIME_FORMAT) : null,
      dateTimeOut: dutySchedule.dateTimeOut ? dutySchedule.dateTimeOut.format(DATE_TIME_FORMAT) : null,
      employee: dutySchedule.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      dutySchedule.employee
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

  protected createFromForm(): IDutySchedule {
    return {
      ...new DutySchedule(),
      id: this.editForm.get(['id'])!.value,
      dateTimeIn: this.editForm.get(['dateTimeIn'])!.value ? dayjs(this.editForm.get(['dateTimeIn'])!.value, DATE_TIME_FORMAT) : undefined,
      dateTimeOut: this.editForm.get(['dateTimeOut'])!.value
        ? dayjs(this.editForm.get(['dateTimeOut'])!.value, DATE_TIME_FORMAT)
        : undefined,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
