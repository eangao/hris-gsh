jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepartmentService } from '../service/department.service';
import { IDepartment, Department } from '../department.model';
import { ICluster } from 'app/entities/cluster/cluster.model';
import { ClusterService } from 'app/entities/cluster/service/cluster.service';

import { DepartmentUpdateComponent } from './department-update.component';

describe('Department Management Update Component', () => {
  let comp: DepartmentUpdateComponent;
  let fixture: ComponentFixture<DepartmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departmentService: DepartmentService;
  let clusterService: ClusterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepartmentUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepartmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentService = TestBed.inject(DepartmentService);
    clusterService = TestBed.inject(ClusterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cluster query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const cluster: ICluster = { id: 26470 };
      department.cluster = cluster;

      const clusterCollection: ICluster[] = [{ id: 90553 }];
      jest.spyOn(clusterService, 'query').mockReturnValue(of(new HttpResponse({ body: clusterCollection })));
      const additionalClusters = [cluster];
      const expectedCollection: ICluster[] = [...additionalClusters, ...clusterCollection];
      jest.spyOn(clusterService, 'addClusterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(clusterService.query).toHaveBeenCalled();
      expect(clusterService.addClusterToCollectionIfMissing).toHaveBeenCalledWith(clusterCollection, ...additionalClusters);
      expect(comp.clustersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const department: IDepartment = { id: 456 };
      const cluster: ICluster = { id: 50317 };
      department.cluster = cluster;

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(department));
      expect(comp.clustersSharedCollection).toContain(cluster);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentService.update).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = new Department();
      jest.spyOn(departmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentService.create).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentService.update).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClusterById', () => {
      it('Should return tracked Cluster primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClusterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
