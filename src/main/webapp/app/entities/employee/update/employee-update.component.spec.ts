jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmployeeService } from '../service/employee.service';
import { IEmployee, Employee } from '../employee.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IBenefits } from 'app/entities/benefits/benefits.model';
import { BenefitsService } from 'app/entities/benefits/service/benefits.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeService: EmployeeService;
  let userService: UserService;
  let designationService: DesignationService;
  let benefitsService: BenefitsService;
  let departmentService: DepartmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EmployeeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);
    designationService = TestBed.inject(DesignationService);
    benefitsService = TestBed.inject(BenefitsService);
    departmentService = TestBed.inject(DepartmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 84478 };
      employee.user = user;

      const userCollection: IUser[] = [{ id: 1447 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Designation query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const designations: IDesignation[] = [{ id: 96982 }];
      employee.designations = designations;

      const designationCollection: IDesignation[] = [{ id: 66817 }];
      jest.spyOn(designationService, 'query').mockReturnValue(of(new HttpResponse({ body: designationCollection })));
      const additionalDesignations = [...designations];
      const expectedCollection: IDesignation[] = [...additionalDesignations, ...designationCollection];
      jest.spyOn(designationService, 'addDesignationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(designationService.query).toHaveBeenCalled();
      expect(designationService.addDesignationToCollectionIfMissing).toHaveBeenCalledWith(designationCollection, ...additionalDesignations);
      expect(comp.designationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Benefits query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const benefits: IBenefits[] = [{ id: 10218 }];
      employee.benefits = benefits;

      const benefitsCollection: IBenefits[] = [{ id: 58413 }];
      jest.spyOn(benefitsService, 'query').mockReturnValue(of(new HttpResponse({ body: benefitsCollection })));
      const additionalBenefits = [...benefits];
      const expectedCollection: IBenefits[] = [...additionalBenefits, ...benefitsCollection];
      jest.spyOn(benefitsService, 'addBenefitsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(benefitsService.query).toHaveBeenCalled();
      expect(benefitsService.addBenefitsToCollectionIfMissing).toHaveBeenCalledWith(benefitsCollection, ...additionalBenefits);
      expect(comp.benefitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const department: IDepartment = { id: 35363 };
      employee.department = department;

      const departmentCollection: IDepartment[] = [{ id: 78278 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, ...additionalDepartments);
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 66698 };
      employee.user = user;
      const designations: IDesignation = { id: 92741 };
      employee.designations = [designations];
      const benefits: IBenefits = { id: 82267 };
      employee.benefits = [benefits];
      const department: IDepartment = { id: 60127 };
      employee.department = department;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(employee));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.designationsSharedCollection).toContain(designations);
      expect(comp.benefitsSharedCollection).toContain(benefits);
      expect(comp.departmentsSharedCollection).toContain(department);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = new Employee();
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeService.create).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDesignationById', () => {
      it('Should return tracked Designation primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDesignationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBenefitsById', () => {
      it('Should return tracked Benefits primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBenefitsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepartmentById', () => {
      it('Should return tracked Department primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepartmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDesignation', () => {
      it('Should return option if no Designation is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDesignation(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Designation for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDesignation(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Designation is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDesignation(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedBenefits', () => {
      it('Should return option if no Benefits is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBenefits(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Benefits for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBenefits(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Benefits is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBenefits(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
