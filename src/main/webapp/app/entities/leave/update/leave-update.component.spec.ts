jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeaveService } from '../service/leave.service';
import { ILeave, Leave } from '../leave.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/service/leave-type.service';

import { LeaveUpdateComponent } from './leave-update.component';

describe('Leave Management Update Component', () => {
  let comp: LeaveUpdateComponent;
  let fixture: ComponentFixture<LeaveUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaveService: LeaveService;
  let employeeService: EmployeeService;
  let leaveTypeService: LeaveTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaveUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LeaveUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaveUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaveService = TestBed.inject(LeaveService);
    employeeService = TestBed.inject(EmployeeService);
    leaveTypeService = TestBed.inject(LeaveTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const leave: ILeave = { id: 456 };
      const employee: IEmployee = { id: 94013 };
      leave.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 73048 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LeaveType query and add missing value', () => {
      const leave: ILeave = { id: 456 };
      const leaveType: ILeaveType = { id: 68447 };
      leave.leaveType = leaveType;

      const leaveTypeCollection: ILeaveType[] = [{ id: 67404 }];
      jest.spyOn(leaveTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: leaveTypeCollection })));
      const additionalLeaveTypes = [leaveType];
      const expectedCollection: ILeaveType[] = [...additionalLeaveTypes, ...leaveTypeCollection];
      jest.spyOn(leaveTypeService, 'addLeaveTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(leaveTypeService.query).toHaveBeenCalled();
      expect(leaveTypeService.addLeaveTypeToCollectionIfMissing).toHaveBeenCalledWith(leaveTypeCollection, ...additionalLeaveTypes);
      expect(comp.leaveTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leave: ILeave = { id: 456 };
      const employee: IEmployee = { id: 72877 };
      leave.employee = employee;
      const leaveType: ILeaveType = { id: 4417 };
      leave.leaveType = leaveType;

      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(leave));
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.leaveTypesSharedCollection).toContain(leaveType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Leave>>();
      const leave = { id: 123 };
      jest.spyOn(leaveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leave }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaveService.update).toHaveBeenCalledWith(leave);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Leave>>();
      const leave = new Leave();
      jest.spyOn(leaveService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leave }));
      saveSubject.complete();

      // THEN
      expect(leaveService.create).toHaveBeenCalledWith(leave);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Leave>>();
      const leave = { id: 123 };
      jest.spyOn(leaveService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leave });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaveService.update).toHaveBeenCalledWith(leave);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLeaveTypeById', () => {
      it('Should return tracked LeaveType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLeaveTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
