import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDependents, getDependentsIdentifier } from '../dependents.model';

export type EntityResponseType = HttpResponse<IDependents>;
export type EntityArrayResponseType = HttpResponse<IDependents[]>;

@Injectable({ providedIn: 'root' })
export class DependentsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dependents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dependents: IDependents): Observable<EntityResponseType> {
    return this.http.post<IDependents>(this.resourceUrl, dependents, { observe: 'response' });
  }

  update(dependents: IDependents): Observable<EntityResponseType> {
    return this.http.put<IDependents>(`${this.resourceUrl}/${getDependentsIdentifier(dependents) as number}`, dependents, {
      observe: 'response',
    });
  }

  partialUpdate(dependents: IDependents): Observable<EntityResponseType> {
    return this.http.patch<IDependents>(`${this.resourceUrl}/${getDependentsIdentifier(dependents) as number}`, dependents, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDependents>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDependents[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDependentsToCollectionIfMissing(
    dependentsCollection: IDependents[],
    ...dependentsToCheck: (IDependents | null | undefined)[]
  ): IDependents[] {
    const dependents: IDependents[] = dependentsToCheck.filter(isPresent);
    if (dependents.length > 0) {
      const dependentsCollectionIdentifiers = dependentsCollection.map(dependentsItem => getDependentsIdentifier(dependentsItem)!);
      const dependentsToAdd = dependents.filter(dependentsItem => {
        const dependentsIdentifier = getDependentsIdentifier(dependentsItem);
        if (dependentsIdentifier == null || dependentsCollectionIdentifiers.includes(dependentsIdentifier)) {
          return false;
        }
        dependentsCollectionIdentifiers.push(dependentsIdentifier);
        return true;
      });
      return [...dependentsToAdd, ...dependentsCollection];
    }
    return dependentsCollection;
  }
}
