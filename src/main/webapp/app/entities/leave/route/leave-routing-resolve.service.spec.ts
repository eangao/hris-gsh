jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeave, Leave } from '../leave.model';
import { LeaveService } from '../service/leave.service';

import { LeaveRoutingResolveService } from './leave-routing-resolve.service';

describe('Leave routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaveRoutingResolveService;
  let service: LeaveService;
  let resultLeave: ILeave | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaveRoutingResolveService);
    service = TestBed.inject(LeaveService);
    resultLeave = undefined;
  });

  describe('resolve', () => {
    it('should return ILeave returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeave = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeave).toEqual({ id: 123 });
    });

    it('should return new ILeave if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeave = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeave).toEqual(new Leave());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Leave })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeave = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeave).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
