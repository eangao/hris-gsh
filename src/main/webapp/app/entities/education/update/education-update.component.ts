import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEducation, Education } from '../education.model';
import { EducationService } from '../service/education.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-education-update',
  templateUrl: './education-update.component.html',
})
export class EducationUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    bachelorDegree: [null, [Validators.required, Validators.maxLength(100)]],
    yearGraduated: [null, [Validators.required]],
    school: [null, [Validators.required, Validators.maxLength(200)]],
    employee: [null, Validators.required],
  });

  constructor(
    protected educationService: EducationService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ education }) => {
      this.updateForm(education);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const education = this.createFromForm();
    if (education.id !== undefined) {
      this.subscribeToSaveResponse(this.educationService.update(education));
    } else {
      this.subscribeToSaveResponse(this.educationService.create(education));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEducation>>): void {
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

  protected updateForm(education: IEducation): void {
    this.editForm.patchValue({
      id: education.id,
      bachelorDegree: education.bachelorDegree,
      yearGraduated: education.yearGraduated,
      school: education.school,
      employee: education.employee,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      education.employee
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

  protected createFromForm(): IEducation {
    return {
      ...new Education(),
      id: this.editForm.get(['id'])!.value,
      bachelorDegree: this.editForm.get(['bachelorDegree'])!.value,
      yearGraduated: this.editForm.get(['yearGraduated'])!.value,
      school: this.editForm.get(['school'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
