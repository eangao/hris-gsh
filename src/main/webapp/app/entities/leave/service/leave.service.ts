import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeave, getLeaveIdentifier } from '../leave.model';

export type EntityResponseType = HttpResponse<ILeave>;
export type EntityArrayResponseType = HttpResponse<ILeave[]>;

@Injectable({ providedIn: 'root' })
export class LeaveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leaves');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leave: ILeave): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leave);
    return this.http
      .post<ILeave>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leave: ILeave): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leave);
    return this.http
      .put<ILeave>(`${this.resourceUrl}/${getLeaveIdentifier(leave) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leave: ILeave): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leave);
    return this.http
      .patch<ILeave>(`${this.resourceUrl}/${getLeaveIdentifier(leave) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeave>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeave[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeaveToCollectionIfMissing(leaveCollection: ILeave[], ...leavesToCheck: (ILeave | null | undefined)[]): ILeave[] {
    const leaves: ILeave[] = leavesToCheck.filter(isPresent);
    if (leaves.length > 0) {
      const leaveCollectionIdentifiers = leaveCollection.map(leaveItem => getLeaveIdentifier(leaveItem)!);
      const leavesToAdd = leaves.filter(leaveItem => {
        const leaveIdentifier = getLeaveIdentifier(leaveItem);
        if (leaveIdentifier == null || leaveCollectionIdentifiers.includes(leaveIdentifier)) {
          return false;
        }
        leaveCollectionIdentifiers.push(leaveIdentifier);
        return true;
      });
      return [...leavesToAdd, ...leaveCollection];
    }
    return leaveCollection;
  }

  protected convertDateFromClient(leave: ILeave): ILeave {
    return Object.assign({}, leave, {
      dateApply: leave.dateApply?.isValid() ? leave.dateApply.format(DATE_FORMAT) : undefined,
      dateStart: leave.dateStart?.isValid() ? leave.dateStart.format(DATE_FORMAT) : undefined,
      dateEnd: leave.dateEnd?.isValid() ? leave.dateEnd.format(DATE_FORMAT) : undefined,
      dateReturn: leave.dateReturn?.isValid() ? leave.dateReturn.format(DATE_FORMAT) : undefined,
      checkupDate: leave.checkupDate?.isValid() ? leave.checkupDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateApply = res.body.dateApply ? dayjs(res.body.dateApply) : undefined;
      res.body.dateStart = res.body.dateStart ? dayjs(res.body.dateStart) : undefined;
      res.body.dateEnd = res.body.dateEnd ? dayjs(res.body.dateEnd) : undefined;
      res.body.dateReturn = res.body.dateReturn ? dayjs(res.body.dateReturn) : undefined;
      res.body.checkupDate = res.body.checkupDate ? dayjs(res.body.checkupDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leave: ILeave) => {
        leave.dateApply = leave.dateApply ? dayjs(leave.dateApply) : undefined;
        leave.dateStart = leave.dateStart ? dayjs(leave.dateStart) : undefined;
        leave.dateEnd = leave.dateEnd ? dayjs(leave.dateEnd) : undefined;
        leave.dateReturn = leave.dateReturn ? dayjs(leave.dateReturn) : undefined;
        leave.checkupDate = leave.checkupDate ? dayjs(leave.checkupDate) : undefined;
      });
    }
    return res;
  }
}
