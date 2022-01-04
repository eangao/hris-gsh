import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDependents, Dependents } from '../dependents.model';
import { DependentsService } from '../service/dependents.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-dependents-update',
  templateUrl: './dependents-update.component.html',
})
export class DependentsUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required, Validators.maxLength(50)]],
    middleName: [null, [Validators.maxLength(50)]],
    lastName: [null, [Validators.required, Validators.maxLength(50)]],
    relation: [null, [Validators.required, Validators.maxLength(50)]],
    employee: [null, Validators.required],
  });

  constructor(
    protected dependentsService: DependentsService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependents }) => {
      this.updateForm(dependents);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dependents = this.createFromForm();
    if (dependents.id !== undefined) {
      this.subscribeToSaveResponse(this.dependentsService.update(dependents));
    } else {
      this.subscribeToSaveResponse(this.dependentsService.create(dependents));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDependents>>): void {
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

  protected updateForm(dependents: IDependents): void {
    this.editForm.patchValue({
      id: dependents.id,
      firstName: dependents.firstName,
      middleName: dependents.middleName,
      lastName: dependents.lastName,
      relation: dependents.relation,
      employee: dependents.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      dependents.employee
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

  protected createFromForm(): IDependents {
    return {
      ...new Dependents(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      relation: this.editForm.get(['relation'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
