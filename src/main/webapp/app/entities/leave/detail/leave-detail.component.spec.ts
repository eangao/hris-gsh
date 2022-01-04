import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveDetailComponent } from './leave-detail.component';

describe('Leave Management Detail Component', () => {
  let comp: LeaveDetailComponent;
  let fixture: ComponentFixture<LeaveDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaveDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leave: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaveDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaveDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leave on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leave).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
