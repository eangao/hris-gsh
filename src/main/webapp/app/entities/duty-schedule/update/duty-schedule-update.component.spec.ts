jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DutyScheduleService } from '../service/duty-schedule.service';
import { IDutySchedule, DutySchedule } from '../duty-schedule.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { DutyScheduleUpdateComponent } from './duty-schedule-update.component';

describe('DutySchedule Management Update Component', () => {
  let comp: DutyScheduleUpdateComponent;
  let fixture: ComponentFixture<DutyScheduleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dutyScheduleService: DutyScheduleService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DutyScheduleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DutyScheduleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DutyScheduleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dutyScheduleService = TestBed.inject(DutyScheduleService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const dutySchedule: IDutySchedule = { id: 456 };
      const employee: IEmployee = { id: 78515 };
      dutySchedule.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 17808 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dutySchedule });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dutySchedule: IDutySchedule = { id: 456 };
      const employee: IEmployee = { id: 4566 };
      dutySchedule.employee = employee;

      activatedRoute.data = of({ dutySchedule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dutySchedule));
      expect(comp.employeesSharedCollection).toContain(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DutySchedule>>();
      const dutySchedule = { id: 123 };
      jest.spyOn(dutyScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dutySchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dutySchedule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dutyScheduleService.update).toHaveBeenCalledWith(dutySchedule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DutySchedule>>();
      const dutySchedule = new DutySchedule();
      jest.spyOn(dutyScheduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dutySchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dutySchedule }));
      saveSubject.complete();

      // THEN
      expect(dutyScheduleService.create).toHaveBeenCalledWith(dutySchedule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DutySchedule>>();
      const dutySchedule = { id: 123 };
      jest.spyOn(dutyScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dutySchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dutyScheduleService.update).toHaveBeenCalledWith(dutySchedule);
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
  });
});
