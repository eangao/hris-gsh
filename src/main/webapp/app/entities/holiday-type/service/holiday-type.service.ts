import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHolidayType, getHolidayTypeIdentifier } from '../holiday-type.model';

export type EntityResponseType = HttpResponse<IHolidayType>;
export type EntityArrayResponseType = HttpResponse<IHolidayType[]>;

@Injectable({ providedIn: 'root' })
export class HolidayTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/holiday-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holidayType: IHolidayType): Observable<EntityResponseType> {
    return this.http.post<IHolidayType>(this.resourceUrl, holidayType, { observe: 'response' });
  }

  update(holidayType: IHolidayType): Observable<EntityResponseType> {
    return this.http.put<IHolidayType>(`${this.resourceUrl}/${getHolidayTypeIdentifier(holidayType) as number}`, holidayType, {
      observe: 'response',
    });
  }

  partialUpdate(holidayType: IHolidayType): Observable<EntityResponseType> {
    return this.http.patch<IHolidayType>(`${this.resourceUrl}/${getHolidayTypeIdentifier(holidayType) as number}`, holidayType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHolidayType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHolidayType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHolidayTypeToCollectionIfMissing(
    holidayTypeCollection: IHolidayType[],
    ...holidayTypesToCheck: (IHolidayType | null | undefined)[]
  ): IHolidayType[] {
    const holidayTypes: IHolidayType[] = holidayTypesToCheck.filter(isPresent);
    if (holidayTypes.length > 0) {
      const holidayTypeCollectionIdentifiers = holidayTypeCollection.map(holidayTypeItem => getHolidayTypeIdentifier(holidayTypeItem)!);
      const holidayTypesToAdd = holidayTypes.filter(holidayTypeItem => {
        const holidayTypeIdentifier = getHolidayTypeIdentifier(holidayTypeItem);
        if (holidayTypeIdentifier == null || holidayTypeCollectionIdentifiers.includes(holidayTypeIdentifier)) {
          return false;
        }
        holidayTypeCollectionIdentifiers.push(holidayTypeIdentifier);
        return true;
      });
      return [...holidayTypesToAdd, ...holidayTypeCollection];
    }
    return holidayTypeCollection;
  }
}
