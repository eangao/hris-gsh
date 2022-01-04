import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHoliday, getHolidayIdentifier } from '../holiday.model';

export type EntityResponseType = HttpResponse<IHoliday>;
export type EntityArrayResponseType = HttpResponse<IHoliday[]>;

@Injectable({ providedIn: 'root' })
export class HolidayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/holidays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holiday: IHoliday): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holiday);
    return this.http
      .post<IHoliday>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(holiday: IHoliday): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holiday);
    return this.http
      .put<IHoliday>(`${this.resourceUrl}/${getHolidayIdentifier(holiday) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(holiday: IHoliday): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holiday);
    return this.http
      .patch<IHoliday>(`${this.resourceUrl}/${getHolidayIdentifier(holiday) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHoliday>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHoliday[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHolidayToCollectionIfMissing(holidayCollection: IHoliday[], ...holidaysToCheck: (IHoliday | null | undefined)[]): IHoliday[] {
    const holidays: IHoliday[] = holidaysToCheck.filter(isPresent);
    if (holidays.length > 0) {
      const holidayCollectionIdentifiers = holidayCollection.map(holidayItem => getHolidayIdentifier(holidayItem)!);
      const holidaysToAdd = holidays.filter(holidayItem => {
        const holidayIdentifier = getHolidayIdentifier(holidayItem);
        if (holidayIdentifier == null || holidayCollectionIdentifiers.includes(holidayIdentifier)) {
          return false;
        }
        holidayCollectionIdentifiers.push(holidayIdentifier);
        return true;
      });
      return [...holidaysToAdd, ...holidayCollection];
    }
    return holidayCollection;
  }

  protected convertDateFromClient(holiday: IHoliday): IHoliday {
    return Object.assign({}, holiday, {
      date: holiday.date?.isValid() ? holiday.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((holiday: IHoliday) => {
        holiday.date = holiday.date ? dayjs(holiday.date) : undefined;
      });
    }
    return res;
  }
}
