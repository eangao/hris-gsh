import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IBenefits } from 'app/entities/benefits/benefits.model';
import { BenefitsService } from 'app/entities/benefits/service/benefits.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { Status } from 'app/entities/enumerations/status.model';
import { EmploymentType } from 'app/entities/enumerations/employment-type.model';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  genderValues = Object.keys(Gender);
  statusValues = Object.keys(Status);
  employmentTypeValues = Object.keys(EmploymentType);

  usersSharedCollection: IUser[] = [];
  designationsSharedCollection: IDesignation[] = [];
  benefitsSharedCollection: IBenefits[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    employeeBiometricId: [null, [Validators.required]],
    username: [null, [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
    email: [null, [Validators.minLength(5), Validators.maxLength(254)]],
    firstName: [null, [Validators.required, Validators.maxLength(50)]],
    middleName: [null, [Validators.maxLength(50)]],
    lastName: [null, [Validators.required, Validators.maxLength(50)]],
    nameSuffix: [null, [Validators.maxLength(15)]],
    birthdate: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    status: [null, [Validators.required]],
    employmentType: [null, [Validators.required]],
    mobileNumber: [null, [Validators.maxLength(20)]],
    dateHired: [],
    dateDeno: [],
    sickLeaveYearlyCredit: [],
    sickLeaveYearlyCreditUsed: [],
    leaveYearlyCredit: [],
    leaveYearlyCreditUsed: [],
    profileImage: [],
    profileImageContentType: [],
    presentAddressStreet: [],
    presentAddressCity: [],
    presentAddressProvince: [],
    presentAddressZipcode: [],
    homeAddressStreet: [],
    homeAddressCity: [],
    homeAddressProvince: [],
    homeAddressZipcode: [],
    user: [null, Validators.required],
    designations: [null, Validators.required],
    benefits: [],
    department: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected designationService: DesignationService,
    protected benefitsService: BenefitsService,
    protected departmentService: DepartmentService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hrisGshApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackDesignationById(index: number, item: IDesignation): number {
    return item.id!;
  }

  trackBenefitsById(index: number, item: IBenefits): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  getSelectedDesignation(option: IDesignation, selectedVals?: IDesignation[]): IDesignation {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedBenefits(option: IBenefits, selectedVals?: IBenefits[]): IBenefits {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      employeeBiometricId: employee.employeeBiometricId,
      username: employee.username,
      email: employee.email,
      firstName: employee.firstName,
      middleName: employee.middleName,
      lastName: employee.lastName,
      nameSuffix: employee.nameSuffix,
      birthdate: employee.birthdate,
      gender: employee.gender,
      status: employee.status,
      employmentType: employee.employmentType,
      mobileNumber: employee.mobileNumber,
      dateHired: employee.dateHired,
      dateDeno: employee.dateDeno,
      sickLeaveYearlyCredit: employee.sickLeaveYearlyCredit,
      sickLeaveYearlyCreditUsed: employee.sickLeaveYearlyCreditUsed,
      leaveYearlyCredit: employee.leaveYearlyCredit,
      leaveYearlyCreditUsed: employee.leaveYearlyCreditUsed,
      profileImage: employee.profileImage,
      profileImageContentType: employee.profileImageContentType,
      presentAddressStreet: employee.presentAddressStreet,
      presentAddressCity: employee.presentAddressCity,
      presentAddressProvince: employee.presentAddressProvince,
      presentAddressZipcode: employee.presentAddressZipcode,
      homeAddressStreet: employee.homeAddressStreet,
      homeAddressCity: employee.homeAddressCity,
      homeAddressProvince: employee.homeAddressProvince,
      homeAddressZipcode: employee.homeAddressZipcode,
      user: employee.user,
      designations: employee.designations,
      benefits: employee.benefits,
      department: employee.department,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, employee.user);
    this.designationsSharedCollection = this.designationService.addDesignationToCollectionIfMissing(
      this.designationsSharedCollection,
      ...(employee.designations ?? [])
    );
    this.benefitsSharedCollection = this.benefitsService.addBenefitsToCollectionIfMissing(
      this.benefitsSharedCollection,
      ...(employee.benefits ?? [])
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      employee.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.designationService
      .query()
      .pipe(map((res: HttpResponse<IDesignation[]>) => res.body ?? []))
      .pipe(
        map((designations: IDesignation[]) =>
          this.designationService.addDesignationToCollectionIfMissing(designations, ...(this.editForm.get('designations')!.value ?? []))
        )
      )
      .subscribe((designations: IDesignation[]) => (this.designationsSharedCollection = designations));

    this.benefitsService
      .query()
      .pipe(map((res: HttpResponse<IBenefits[]>) => res.body ?? []))
      .pipe(
        map((benefits: IBenefits[]) =>
          this.benefitsService.addBenefitsToCollectionIfMissing(benefits, ...(this.editForm.get('benefits')!.value ?? []))
        )
      )
      .subscribe((benefits: IBenefits[]) => (this.benefitsSharedCollection = benefits));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      employeeBiometricId: this.editForm.get(['employeeBiometricId'])!.value,
      username: this.editForm.get(['username'])!.value,
      email: this.editForm.get(['email'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      nameSuffix: this.editForm.get(['nameSuffix'])!.value,
      birthdate: this.editForm.get(['birthdate'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      status: this.editForm.get(['status'])!.value,
      employmentType: this.editForm.get(['employmentType'])!.value,
      mobileNumber: this.editForm.get(['mobileNumber'])!.value,
      dateHired: this.editForm.get(['dateHired'])!.value,
      dateDeno: this.editForm.get(['dateDeno'])!.value,
      sickLeaveYearlyCredit: this.editForm.get(['sickLeaveYearlyCredit'])!.value,
      sickLeaveYearlyCreditUsed: this.editForm.get(['sickLeaveYearlyCreditUsed'])!.value,
      leaveYearlyCredit: this.editForm.get(['leaveYearlyCredit'])!.value,
      leaveYearlyCreditUsed: this.editForm.get(['leaveYearlyCreditUsed'])!.value,
      profileImageContentType: this.editForm.get(['profileImageContentType'])!.value,
      profileImage: this.editForm.get(['profileImage'])!.value,
      presentAddressStreet: this.editForm.get(['presentAddressStreet'])!.value,
      presentAddressCity: this.editForm.get(['presentAddressCity'])!.value,
      presentAddressProvince: this.editForm.get(['presentAddressProvince'])!.value,
      presentAddressZipcode: this.editForm.get(['presentAddressZipcode'])!.value,
      homeAddressStreet: this.editForm.get(['homeAddressStreet'])!.value,
      homeAddressCity: this.editForm.get(['homeAddressCity'])!.value,
      homeAddressProvince: this.editForm.get(['homeAddressProvince'])!.value,
      homeAddressZipcode: this.editForm.get(['homeAddressZipcode'])!.value,
      user: this.editForm.get(['user'])!.value,
      designations: this.editForm.get(['designations'])!.value,
      benefits: this.editForm.get(['benefits'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}
