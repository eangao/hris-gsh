jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrainingHistoryService } from '../service/training-history.service';
import { ITrainingHistory, TrainingHistory } from '../training-history.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { TrainingHistoryUpdateComponent } from './training-history-update.component';

describe('TrainingHistory Management Update Component', () => {
  let comp: TrainingHistoryUpdateComponent;
  let fixture: ComponentFixture<TrainingHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trainingHistoryService: TrainingHistoryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TrainingHistoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TrainingHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainingHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trainingHistoryService = TestBed.inject(TrainingHistoryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const trainingHistory: ITrainingHistory = { id: 456 };
      const employee: IEmployee = { id: 87900 };
      trainingHistory.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 12546 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trainingHistory });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trainingHistory: ITrainingHistory = { id: 456 };
      const employee: IEmployee = { id: 47648 };
      trainingHistory.employee = employee;

      activatedRoute.data = of({ trainingHistory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(trainingHistory));
      expect(comp.employeesSharedCollection).toContain(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrainingHistory>>();
      const trainingHistory = { id: 123 };
      jest.spyOn(trainingHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainingHistory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(trainingHistoryService.update).toHaveBeenCalledWith(trainingHistory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrainingHistory>>();
      const trainingHistory = new TrainingHistory();
      jest.spyOn(trainingHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainingHistory }));
      saveSubject.complete();

      // THEN
      expect(trainingHistoryService.create).toHaveBeenCalledWith(trainingHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TrainingHistory>>();
      const trainingHistory = { id: 123 };
      jest.spyOn(trainingHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainingHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trainingHistoryService.update).toHaveBeenCalledWith(trainingHistory);
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
