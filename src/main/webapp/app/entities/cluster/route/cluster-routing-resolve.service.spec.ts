jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICluster, Cluster } from '../cluster.model';
import { ClusterService } from '../service/cluster.service';

import { ClusterRoutingResolveService } from './cluster-routing-resolve.service';

describe('Cluster routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ClusterRoutingResolveService;
  let service: ClusterService;
  let resultCluster: ICluster | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ClusterRoutingResolveService);
    service = TestBed.inject(ClusterService);
    resultCluster = undefined;
  });

  describe('resolve', () => {
    it('should return ICluster returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCluster = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCluster).toEqual({ id: 123 });
    });

    it('should return new ICluster if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCluster = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCluster).toEqual(new Cluster());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cluster })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCluster = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCluster).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
