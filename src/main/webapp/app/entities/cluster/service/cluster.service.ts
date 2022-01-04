import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICluster, getClusterIdentifier } from '../cluster.model';

export type EntityResponseType = HttpResponse<ICluster>;
export type EntityArrayResponseType = HttpResponse<ICluster[]>;

@Injectable({ providedIn: 'root' })
export class ClusterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clusters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cluster: ICluster): Observable<EntityResponseType> {
    return this.http.post<ICluster>(this.resourceUrl, cluster, { observe: 'response' });
  }

  update(cluster: ICluster): Observable<EntityResponseType> {
    return this.http.put<ICluster>(`${this.resourceUrl}/${getClusterIdentifier(cluster) as number}`, cluster, { observe: 'response' });
  }

  partialUpdate(cluster: ICluster): Observable<EntityResponseType> {
    return this.http.patch<ICluster>(`${this.resourceUrl}/${getClusterIdentifier(cluster) as number}`, cluster, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICluster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICluster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClusterToCollectionIfMissing(clusterCollection: ICluster[], ...clustersToCheck: (ICluster | null | undefined)[]): ICluster[] {
    const clusters: ICluster[] = clustersToCheck.filter(isPresent);
    if (clusters.length > 0) {
      const clusterCollectionIdentifiers = clusterCollection.map(clusterItem => getClusterIdentifier(clusterItem)!);
      const clustersToAdd = clusters.filter(clusterItem => {
        const clusterIdentifier = getClusterIdentifier(clusterItem);
        if (clusterIdentifier == null || clusterCollectionIdentifiers.includes(clusterIdentifier)) {
          return false;
        }
        clusterCollectionIdentifiers.push(clusterIdentifier);
        return true;
      });
      return [...clustersToAdd, ...clusterCollection];
    }
    return clusterCollection;
  }
}
