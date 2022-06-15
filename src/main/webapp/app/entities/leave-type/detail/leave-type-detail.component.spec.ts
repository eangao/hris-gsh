import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveTypeDetailComponent } from './leave-type-detail.component';

describe('LeaveType Management Detail Component', () => {
  let comp: LeaveTypeDetailComponent;
  let fixture: ComponentFixture<LeaveTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaveTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaveType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaveTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaveTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaveType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaveType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
