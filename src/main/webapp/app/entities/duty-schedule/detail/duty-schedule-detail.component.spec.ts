import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DutyScheduleDetailComponent } from './duty-schedule-detail.component';

describe('DutySchedule Management Detail Component', () => {
  let comp: DutyScheduleDetailComponent;
  let fixture: ComponentFixture<DutyScheduleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DutyScheduleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dutySchedule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DutyScheduleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DutyScheduleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dutySchedule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dutySchedule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
