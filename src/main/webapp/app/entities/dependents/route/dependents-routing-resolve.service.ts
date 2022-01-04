import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDependents, Dependents } from '../dependents.model';
import { DependentsService } from '../service/dependents.service';

@Injectable({ providedIn: 'root' })
export class DependentsRoutingResolveService implements Resolve<IDependents> {
  constructor(protected service: DependentsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDependents> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dependents: HttpResponse<Dependents>) => {
          if (dependents.body) {
            return of(dependents.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dependents());
  }
}
