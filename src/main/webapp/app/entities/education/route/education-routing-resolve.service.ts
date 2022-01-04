import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEducation, Education } from '../education.model';
import { EducationService } from '../service/education.service';

@Injectable({ providedIn: 'root' })
export class EducationRoutingResolveService implements Resolve<IEducation> {
  constructor(protected service: EducationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEducation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((education: HttpResponse<Education>) => {
          if (education.body) {
            return of(education.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Education());
  }
}
