jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDesignation, Designation } from '../designation.model';
import { DesignationService } from '../service/designation.service';

import { DesignationRoutingResolveService } from './designation-routing-resolve.service';

describe('Designation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DesignationRoutingResolveService;
  let service: DesignationService;
  let resultDesignation: IDesignation | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DesignationRoutingResolveService);
    service = TestBed.inject(DesignationService);
    resultDesignation = undefined;
  });

  describe('resolve', () => {
    it('should return IDesignation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDesignation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDesignation).toEqual({ id: 123 });
    });

    it('should return new IDesignation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDesignation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDesignation).toEqual(new Designation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Designation })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDesignation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDesignation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
