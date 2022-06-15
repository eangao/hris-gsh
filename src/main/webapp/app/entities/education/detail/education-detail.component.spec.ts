import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EducationDetailComponent } from './education-detail.component';

describe('Education Management Detail Component', () => {
  let comp: EducationDetailComponent;
  let fixture: ComponentFixture<EducationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EducationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ education: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EducationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EducationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load education on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.education).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
