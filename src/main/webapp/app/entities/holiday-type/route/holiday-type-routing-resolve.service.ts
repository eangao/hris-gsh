import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHolidayType, HolidayType } from '../holiday-type.model';
import { HolidayTypeService } from '../service/holiday-type.service';

@Injectable({ providedIn: 'root' })
export class HolidayTypeRoutingResolveService implements Resolve<IHolidayType> {
  constructor(protected service: HolidayTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHolidayType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((holidayType: HttpResponse<HolidayType>) => {
          if (holidayType.body) {
            return of(holidayType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HolidayType());
  }
}
