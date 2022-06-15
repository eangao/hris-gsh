import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BenefitsDetailComponent } from './benefits-detail.component';

describe('Benefits Management Detail Component', () => {
  let comp: BenefitsDetailComponent;
  let fixture: ComponentFixture<BenefitsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BenefitsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ benefits: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BenefitsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BenefitsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load benefits on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.benefits).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
