import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDutySchedule, getDutyScheduleIdentifier } from '../duty-schedule.model';

export type EntityResponseType = HttpResponse<IDutySchedule>;
export type EntityArrayResponseType = HttpResponse<IDutySchedule[]>;

@Injectable({ providedIn: 'root' })
export class DutyScheduleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/duty-schedules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dutySchedule: IDutySchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dutySchedule);
    return this.http
      .post<IDutySchedule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dutySchedule: IDutySchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dutySchedule);
    return this.http
      .put<IDutySchedule>(`${this.resourceUrl}/${getDutyScheduleIdentifier(dutySchedule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dutySchedule: IDutySchedule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dutySchedule);
    return this.http
      .patch<IDutySchedule>(`${this.resourceUrl}/${getDutyScheduleIdentifier(dutySchedule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDutySchedule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDutySchedule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDutyScheduleToCollectionIfMissing(
    dutyScheduleCollection: IDutySchedule[],
    ...dutySchedulesToCheck: (IDutySchedule | null | undefined)[]
  ): IDutySchedule[] {
    const dutySchedules: IDutySchedule[] = dutySchedulesToCheck.filter(isPresent);
    if (dutySchedules.length > 0) {
      const dutyScheduleCollectionIdentifiers = dutyScheduleCollection.map(
        dutyScheduleItem => getDutyScheduleIdentifier(dutyScheduleItem)!
      );
      const dutySchedulesToAdd = dutySchedules.filter(dutyScheduleItem => {
        const dutyScheduleIdentifier = getDutyScheduleIdentifier(dutyScheduleItem);
        if (dutyScheduleIdentifier == null || dutyScheduleCollectionIdentifiers.includes(dutyScheduleIdentifier)) {
          return false;
        }
        dutyScheduleCollectionIdentifiers.push(dutyScheduleIdentifier);
        return true;
      });
      return [...dutySchedulesToAdd, ...dutyScheduleCollection];
    }
    return dutyScheduleCollection;
  }

  protected convertDateFromClient(dutySchedule: IDutySchedule): IDutySchedule {
    return Object.assign({}, dutySchedule, {
      dateTimeIn: dutySchedule.dateTimeIn?.isValid() ? dutySchedule.dateTimeIn.toJSON() : undefined,
      dateTimeOut: dutySchedule.dateTimeOut?.isValid() ? dutySchedule.dateTimeOut.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateTimeIn = res.body.dateTimeIn ? dayjs(res.body.dateTimeIn) : undefined;
      res.body.dateTimeOut = res.body.dateTimeOut ? dayjs(res.body.dateTimeOut) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dutySchedule: IDutySchedule) => {
        dutySchedule.dateTimeIn = dutySchedule.dateTimeIn ? dayjs(dutySchedule.dateTimeIn) : undefined;
        dutySchedule.dateTimeOut = dutySchedule.dateTimeOut ? dayjs(dutySchedule.dateTimeOut) : undefined;
      });
    }
    return res;
  }
}
