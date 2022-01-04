import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeave, Leave } from '../leave.model';
import { LeaveService } from '../service/leave.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/service/leave-type.service';

@Component({
  selector: 'jhi-leave-update',
  templateUrl: './leave-update.component.html',
})
export class LeaveUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];
  leaveTypesSharedCollection: ILeaveType[] = [];

  editForm = this.fb.group({
    id: [],
    dateApply: [],
    dateStart: [],
    dateEnd: [],
    dateReturn: [],
    checkupDate: [],
    convalescingPeriod: [],
    diagnosis: [],
    employee: [null, Validators.required],
    leaveType: [null, Validators.required],
  });

  constructor(
    protected leaveService: LeaveService,
    protected employeeService: EmployeeService,
    protected leaveTypeService: LeaveTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leave }) => {
      this.updateForm(leave);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leave = this.createFromForm();
    if (leave.id !== undefined) {
      this.subscribeToSaveResponse(this.leaveService.update(leave));
    } else {
      this.subscribeToSaveResponse(this.leaveService.create(leave));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  trackLeaveTypeById(index: number, item: ILeaveType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeave>>): void {
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

  protected updateForm(leave: ILeave): void {
    this.editForm.patchValue({
      id: leave.id,
      dateApply: leave.dateApply,
      dateStart: leave.dateStart,
      dateEnd: leave.dateEnd,
      dateReturn: leave.dateReturn,
      checkupDate: leave.checkupDate,
      convalescingPeriod: leave.convalescingPeriod,
      diagnosis: leave.diagnosis,
      employee: leave.employee,
      leaveType: leave.leaveType,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(this.employeesSharedCollection, leave.employee);
    this.leaveTypesSharedCollection = this.leaveTypeService.addLeaveTypeToCollectionIfMissing(
      this.leaveTypesSharedCollection,
      leave.leaveType
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

    this.leaveTypeService
      .query()
      .pipe(map((res: HttpResponse<ILeaveType[]>) => res.body ?? []))
      .pipe(
        map((leaveTypes: ILeaveType[]) =>
          this.leaveTypeService.addLeaveTypeToCollectionIfMissing(leaveTypes, this.editForm.get('leaveType')!.value)
        )
      )
      .subscribe((leaveTypes: ILeaveType[]) => (this.leaveTypesSharedCollection = leaveTypes));
  }

  protected createFromForm(): ILeave {
    return {
      ...new Leave(),
      id: this.editForm.get(['id'])!.value,
      dateApply: this.editForm.get(['dateApply'])!.value,
      dateStart: this.editForm.get(['dateStart'])!.value,
      dateEnd: this.editForm.get(['dateEnd'])!.value,
      dateReturn: this.editForm.get(['dateReturn'])!.value,
      checkupDate: this.editForm.get(['checkupDate'])!.value,
      convalescingPeriod: this.editForm.get(['convalescingPeriod'])!.value,
      diagnosis: this.editForm.get(['diagnosis'])!.value,
      employee: this.editForm.get(['employee'])!.value,
      leaveType: this.editForm.get(['leaveType'])!.value,
    };
  }
}
