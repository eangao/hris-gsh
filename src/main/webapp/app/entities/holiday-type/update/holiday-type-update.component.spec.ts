jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HolidayTypeService } from '../service/holiday-type.service';
import { IHolidayType, HolidayType } from '../holiday-type.model';

import { HolidayTypeUpdateComponent } from './holiday-type-update.component';

describe('HolidayType Management Update Component', () => {
  let comp: HolidayTypeUpdateComponent;
  let fixture: ComponentFixture<HolidayTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let holidayTypeService: HolidayTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HolidayTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(HolidayTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HolidayTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    holidayTypeService = TestBed.inject(HolidayTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const holidayType: IHolidayType = { id: 456 };

      activatedRoute.data = of({ holidayType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(holidayType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HolidayType>>();
      const holidayType = { id: 123 };
      jest.spyOn(holidayTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holidayType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holidayType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(holidayTypeService.update).toHaveBeenCalledWith(holidayType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HolidayType>>();
      const holidayType = new HolidayType();
      jest.spyOn(holidayTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holidayType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holidayType }));
      saveSubject.complete();

      // THEN
      expect(holidayTypeService.create).toHaveBeenCalledWith(holidayType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HolidayType>>();
      const holidayType = { id: 123 };
      jest.spyOn(holidayTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holidayType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(holidayTypeService.update).toHaveBeenCalledWith(holidayType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
