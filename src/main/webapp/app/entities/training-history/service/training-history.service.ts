import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrainingHistory, getTrainingHistoryIdentifier } from '../training-history.model';

export type EntityResponseType = HttpResponse<ITrainingHistory>;
export type EntityArrayResponseType = HttpResponse<ITrainingHistory[]>;

@Injectable({ providedIn: 'root' })
export class TrainingHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/training-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trainingHistory: ITrainingHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingHistory);
    return this.http
      .post<ITrainingHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trainingHistory: ITrainingHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingHistory);
    return this.http
      .put<ITrainingHistory>(`${this.resourceUrl}/${getTrainingHistoryIdentifier(trainingHistory) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(trainingHistory: ITrainingHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingHistory);
    return this.http
      .patch<ITrainingHistory>(`${this.resourceUrl}/${getTrainingHistoryIdentifier(trainingHistory) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrainingHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrainingHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrainingHistoryToCollectionIfMissing(
    trainingHistoryCollection: ITrainingHistory[],
    ...trainingHistoriesToCheck: (ITrainingHistory | null | undefined)[]
  ): ITrainingHistory[] {
    const trainingHistories: ITrainingHistory[] = trainingHistoriesToCheck.filter(isPresent);
    if (trainingHistories.length > 0) {
      const trainingHistoryCollectionIdentifiers = trainingHistoryCollection.map(
        trainingHistoryItem => getTrainingHistoryIdentifier(trainingHistoryItem)!
      );
      const trainingHistoriesToAdd = trainingHistories.filter(trainingHistoryItem => {
        const trainingHistoryIdentifier = getTrainingHistoryIdentifier(trainingHistoryItem);
        if (trainingHistoryIdentifier == null || trainingHistoryCollectionIdentifiers.includes(trainingHistoryIdentifier)) {
          return false;
        }
        trainingHistoryCollectionIdentifiers.push(trainingHistoryIdentifier);
        return true;
      });
      return [...trainingHistoriesToAdd, ...trainingHistoryCollection];
    }
    return trainingHistoryCollection;
  }

  protected convertDateFromClient(trainingHistory: ITrainingHistory): ITrainingHistory {
    return Object.assign({}, trainingHistory, {
      date: trainingHistory.date?.isValid() ? trainingHistory.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((trainingHistory: ITrainingHistory) => {
        trainingHistory.date = trainingHistory.date ? dayjs(trainingHistory.date) : undefined;
      });
    }
    return res;
  }
}
