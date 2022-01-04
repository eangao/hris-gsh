jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DailyTimeRecordService } from '../service/daily-time-record.service';
import { IDailyTimeRecord, DailyTimeRecord } from '../daily-time-record.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { DailyTimeRecordUpdateComponent } from './daily-time-record-update.component';

describe('DailyTimeRecord Management Update Component', () => {
  let comp: DailyTimeRecordUpdateComponent;
  let fixture: ComponentFixture<DailyTimeRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dailyTimeRecordService: DailyTimeRecordService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DailyTimeRecordUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DailyTimeRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DailyTimeRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dailyTimeRecordService = TestBed.inject(DailyTimeRecordService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const dailyTimeRecord: IDailyTimeRecord = { id: 456 };
      const employee: IEmployee = { id: 36317 };
      dailyTimeRecord.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 85320 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dailyTimeRecord });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dailyTimeRecord: IDailyTimeRecord = { id: 456 };
      const employee: IEmployee = { id: 73880 };
      dailyTimeRecord.employee = employee;

      activatedRoute.data = of({ dailyTimeRecord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dailyTimeRecord));
      expect(comp.employeesSharedCollection).toContain(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DailyTimeRecord>>();
      const dailyTimeRecord = { id: 123 };
      jest.spyOn(dailyTimeRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyTimeRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dailyTimeRecord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dailyTimeRecordService.update).toHaveBeenCalledWith(dailyTimeRecord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DailyTimeRecord>>();
      const dailyTimeRecord = new DailyTimeRecord();
      jest.spyOn(dailyTimeRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyTimeRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dailyTimeRecord }));
      saveSubject.complete();

      // THEN
      expect(dailyTimeRecordService.create).toHaveBeenCalledWith(dailyTimeRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DailyTimeRecord>>();
      const dailyTimeRecord = { id: 123 };
      jest.spyOn(dailyTimeRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyTimeRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dailyTimeRecordService.update).toHaveBeenCalledWith(dailyTimeRecord);
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
