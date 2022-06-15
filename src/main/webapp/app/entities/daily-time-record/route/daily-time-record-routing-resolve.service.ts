import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDailyTimeRecord, DailyTimeRecord } from '../daily-time-record.model';
import { DailyTimeRecordService } from '../service/daily-time-record.service';

@Injectable({ providedIn: 'root' })
export class DailyTimeRecordRoutingResolveService implements Resolve<IDailyTimeRecord> {
  constructor(protected service: DailyTimeRecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDailyTimeRecord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dailyTimeRecord: HttpResponse<DailyTimeRecord>) => {
          if (dailyTimeRecord.body) {
            return of(dailyTimeRecord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DailyTimeRecord());
  }
}
