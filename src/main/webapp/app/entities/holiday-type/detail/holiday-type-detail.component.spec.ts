import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HolidayTypeDetailComponent } from './holiday-type-detail.component';

describe('HolidayType Management Detail Component', () => {
  let comp: HolidayTypeDetailComponent;
  let fixture: ComponentFixture<HolidayTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HolidayTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ holidayType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HolidayTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HolidayTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load holidayType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.holidayType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
