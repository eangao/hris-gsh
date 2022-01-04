import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeave, Leave } from '../leave.model';
import { LeaveService } from '../service/leave.service';

@Injectable({ providedIn: 'root' })
export class LeaveRoutingResolveService implements Resolve<ILeave> {
  constructor(protected service: LeaveService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeave> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leave: HttpResponse<Leave>) => {
          if (leave.body) {
            return of(leave.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Leave());
  }
}
