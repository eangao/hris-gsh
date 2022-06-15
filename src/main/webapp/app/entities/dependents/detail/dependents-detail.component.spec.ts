import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DependentsDetailComponent } from './dependents-detail.component';

describe('Dependents Management Detail Component', () => {
  let comp: DependentsDetailComponent;
  let fixture: ComponentFixture<DependentsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DependentsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dependents: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DependentsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DependentsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dependents on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dependents).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
