jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClusterService } from '../service/cluster.service';
import { ICluster, Cluster } from '../cluster.model';

import { ClusterUpdateComponent } from './cluster-update.component';

describe('Cluster Management Update Component', () => {
  let comp: ClusterUpdateComponent;
  let fixture: ComponentFixture<ClusterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clusterService: ClusterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClusterUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClusterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClusterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clusterService = TestBed.inject(ClusterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cluster: ICluster = { id: 456 };

      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cluster));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cluster>>();
      const cluster = { id: 123 };
      jest.spyOn(clusterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cluster }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(clusterService.update).toHaveBeenCalledWith(cluster);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cluster>>();
      const cluster = new Cluster();
      jest.spyOn(clusterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cluster }));
      saveSubject.complete();

      // THEN
      expect(clusterService.create).toHaveBeenCalledWith(cluster);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cluster>>();
      const cluster = { id: 123 };
      jest.spyOn(clusterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cluster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clusterService.update).toHaveBeenCalledWith(cluster);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
