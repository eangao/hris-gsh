import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEducation, getEducationIdentifier } from '../education.model';

export type EntityResponseType = HttpResponse<IEducation>;
export type EntityArrayResponseType = HttpResponse<IEducation[]>;

@Injectable({ providedIn: 'root' })
export class EducationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/educations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(education: IEducation): Observable<EntityResponseType> {
    return this.http.post<IEducation>(this.resourceUrl, education, { observe: 'response' });
  }

  update(education: IEducation): Observable<EntityResponseType> {
    return this.http.put<IEducation>(`${this.resourceUrl}/${getEducationIdentifier(education) as number}`, education, {
      observe: 'response',
    });
  }

  partialUpdate(education: IEducation): Observable<EntityResponseType> {
    return this.http.patch<IEducation>(`${this.resourceUrl}/${getEducationIdentifier(education) as number}`, education, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEducation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEducation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEducationToCollectionIfMissing(
    educationCollection: IEducation[],
    ...educationsToCheck: (IEducation | null | undefined)[]
  ): IEducation[] {
    const educations: IEducation[] = educationsToCheck.filter(isPresent);
    if (educations.length > 0) {
      const educationCollectionIdentifiers = educationCollection.map(educationItem => getEducationIdentifier(educationItem)!);
      const educationsToAdd = educations.filter(educationItem => {
        const educationIdentifier = getEducationIdentifier(educationItem);
        if (educationIdentifier == null || educationCollectionIdentifiers.includes(educationIdentifier)) {
          return false;
        }
        educationCollectionIdentifiers.push(educationIdentifier);
        return true;
      });
      return [...educationsToAdd, ...educationCollection];
    }
    return educationCollection;
  }
}
