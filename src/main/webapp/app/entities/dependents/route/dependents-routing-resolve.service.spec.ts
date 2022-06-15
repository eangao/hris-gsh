jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDependents, Dependents } from '../dependents.model';
import { DependentsService } from '../service/dependents.service';

import { DependentsRoutingResolveService } from './dependents-routing-resolve.service';

describe('Dependents routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DependentsRoutingResolveService;
  let service: DependentsService;
  let resultDependents: IDependents | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DependentsRoutingResolveService);
    service = TestBed.inject(DependentsService);
    resultDependents = undefined;
  });

  describe('resolve', () => {
    it('should return IDependents returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDependents = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDependents).toEqual({ id: 123 });
    });

    it('should return new IDependents if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDependents = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDependents).toEqual(new Dependents());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Dependents })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDependents = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDependents).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
