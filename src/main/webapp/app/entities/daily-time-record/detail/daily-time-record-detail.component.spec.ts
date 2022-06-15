import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DailyTimeRecordDetailComponent } from './daily-time-record-detail.component';

describe('DailyTimeRecord Management Detail Component', () => {
  let comp: DailyTimeRecordDetailComponent;
  let fixture: ComponentFixture<DailyTimeRecordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DailyTimeRecordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dailyTimeRecord: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DailyTimeRecordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DailyTimeRecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dailyTimeRecord on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dailyTimeRecord).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
