import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDailyTimeRecord, getDailyTimeRecordIdentifier } from '../daily-time-record.model';

export type EntityResponseType = HttpResponse<IDailyTimeRecord>;
export type EntityArrayResponseType = HttpResponse<IDailyTimeRecord[]>;

@Injectable({ providedIn: 'root' })
export class DailyTimeRecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daily-time-records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dailyTimeRecord: IDailyTimeRecord): Observable<EntityResponseType> {
    return this.http.post<IDailyTimeRecord>(this.resourceUrl, dailyTimeRecord, { observe: 'response' });
  }

  update(dailyTimeRecord: IDailyTimeRecord): Observable<EntityResponseType> {
    return this.http.put<IDailyTimeRecord>(
      `${this.resourceUrl}/${getDailyTimeRecordIdentifier(dailyTimeRecord) as number}`,
      dailyTimeRecord,
      { observe: 'response' }
    );
  }

  partialUpdate(dailyTimeRecord: IDailyTimeRecord): Observable<EntityResponseType> {
    return this.http.patch<IDailyTimeRecord>(
      `${this.resourceUrl}/${getDailyTimeRecordIdentifier(dailyTimeRecord) as number}`,
      dailyTimeRecord,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDailyTimeRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDailyTimeRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDailyTimeRecordToCollectionIfMissing(
    dailyTimeRecordCollection: IDailyTimeRecord[],
    ...dailyTimeRecordsToCheck: (IDailyTimeRecord | null | undefined)[]
  ): IDailyTimeRecord[] {
    const dailyTimeRecords: IDailyTimeRecord[] = dailyTimeRecordsToCheck.filter(isPresent);
    if (dailyTimeRecords.length > 0) {
      const dailyTimeRecordCollectionIdentifiers = dailyTimeRecordCollection.map(
        dailyTimeRecordItem => getDailyTimeRecordIdentifier(dailyTimeRecordItem)!
      );
      const dailyTimeRecordsToAdd = dailyTimeRecords.filter(dailyTimeRecordItem => {
        const dailyTimeRecordIdentifier = getDailyTimeRecordIdentifier(dailyTimeRecordItem);
        if (dailyTimeRecordIdentifier == null || dailyTimeRecordCollectionIdentifiers.includes(dailyTimeRecordIdentifier)) {
          return false;
        }
        dailyTimeRecordCollectionIdentifiers.push(dailyTimeRecordIdentifier);
        return true;
      });
      return [...dailyTimeRecordsToAdd, ...dailyTimeRecordCollection];
    }
    return dailyTimeRecordCollection;
  }
}
