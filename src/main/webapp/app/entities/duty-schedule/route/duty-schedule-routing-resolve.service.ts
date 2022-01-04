import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDutySchedule, DutySchedule } from '../duty-schedule.model';
import { DutyScheduleService } from '../service/duty-schedule.service';

@Injectable({ providedIn: 'root' })
export class DutyScheduleRoutingResolveService implements Resolve<IDutySchedule> {
  constructor(protected service: DutyScheduleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDutySchedule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dutySchedule: HttpResponse<DutySchedule>) => {
          if (dutySchedule.body) {
            return of(dutySchedule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DutySchedule());
  }
}
