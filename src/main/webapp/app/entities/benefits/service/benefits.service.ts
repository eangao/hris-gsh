import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBenefits, getBenefitsIdentifier } from '../benefits.model';

export type EntityResponseType = HttpResponse<IBenefits>;
export type EntityArrayResponseType = HttpResponse<IBenefits[]>;

@Injectable({ providedIn: 'root' })
export class BenefitsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/benefits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(benefits: IBenefits): Observable<EntityResponseType> {
    return this.http.post<IBenefits>(this.resourceUrl, benefits, { observe: 'response' });
  }

  update(benefits: IBenefits): Observable<EntityResponseType> {
    return this.http.put<IBenefits>(`${this.resourceUrl}/${getBenefitsIdentifier(benefits) as number}`, benefits, { observe: 'response' });
  }

  partialUpdate(benefits: IBenefits): Observable<EntityResponseType> {
    return this.http.patch<IBenefits>(`${this.resourceUrl}/${getBenefitsIdentifier(benefits) as number}`, benefits, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBenefits>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBenefits[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBenefitsToCollectionIfMissing(benefitsCollection: IBenefits[], ...benefitsToCheck: (IBenefits | null | undefined)[]): IBenefits[] {
    const benefits: IBenefits[] = benefitsToCheck.filter(isPresent);
    if (benefits.length > 0) {
      const benefitsCollectionIdentifiers = benefitsCollection.map(benefitsItem => getBenefitsIdentifier(benefitsItem)!);
      const benefitsToAdd = benefits.filter(benefitsItem => {
        const benefitsIdentifier = getBenefitsIdentifier(benefitsItem);
        if (benefitsIdentifier == null || benefitsCollectionIdentifiers.includes(benefitsIdentifier)) {
          return false;
        }
        benefitsCollectionIdentifiers.push(benefitsIdentifier);
        return true;
      });
      return [...benefitsToAdd, ...benefitsCollection];
    }
    return benefitsCollection;
  }
}
