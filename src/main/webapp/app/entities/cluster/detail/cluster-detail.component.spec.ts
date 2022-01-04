import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClusterDetailComponent } from './cluster-detail.component';

describe('Cluster Management Detail Component', () => {
  let comp: ClusterDetailComponent;
  let fixture: ComponentFixture<ClusterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClusterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cluster: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClusterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClusterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cluster on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cluster).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
