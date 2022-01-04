jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHolidayType, HolidayType } from '../holiday-type.model';
import { HolidayTypeService } from '../service/holiday-type.service';

import { HolidayTypeRoutingResolveService } from './holiday-type-routing-resolve.service';

describe('HolidayType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HolidayTypeRoutingResolveService;
  let service: HolidayTypeService;
  let resultHolidayType: IHolidayType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(HolidayTypeRoutingResolveService);
    service = TestBed.inject(HolidayTypeService);
    resultHolidayType = undefined;
  });

  describe('resolve', () => {
    it('should return IHolidayType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHolidayType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHolidayType).toEqual({ id: 123 });
    });

    it('should return new IHolidayType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHolidayType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHolidayType).toEqual(new HolidayType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as HolidayType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHolidayType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHolidayType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
