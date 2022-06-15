import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrainingHistory, TrainingHistory } from '../training-history.model';
import { TrainingHistoryService } from '../service/training-history.service';

@Injectable({ providedIn: 'root' })
export class TrainingHistoryRoutingResolveService implements Resolve<ITrainingHistory> {
  constructor(protected service: TrainingHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrainingHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trainingHistory: HttpResponse<TrainingHistory>) => {
          if (trainingHistory.body) {
            return of(trainingHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TrainingHistory());
  }
}
