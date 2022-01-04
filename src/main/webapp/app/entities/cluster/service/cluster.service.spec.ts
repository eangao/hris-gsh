import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICluster, Cluster } from '../cluster.model';

import { ClusterService } from './cluster.service';

describe('Cluster Service', () => {
  let service: ClusterService;
  let httpMock: HttpTestingController;
  let elemDefault: ICluster;
  let expectedResult: ICluster | ICluster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClusterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Cluster', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cluster()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cluster', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cluster', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Cluster()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cluster', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Cluster', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClusterToCollectionIfMissing', () => {
      it('should add a Cluster to an empty array', () => {
        const cluster: ICluster = { id: 123 };
        expectedResult = service.addClusterToCollectionIfMissing([], cluster);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cluster);
      });

      it('should not add a Cluster to an array that contains it', () => {
        const cluster: ICluster = { id: 123 };
        const clusterCollection: ICluster[] = [
          {
            ...cluster,
          },
          { id: 456 },
        ];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, cluster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cluster to an array that doesn't contain it", () => {
        const cluster: ICluster = { id: 123 };
        const clusterCollection: ICluster[] = [{ id: 456 }];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, cluster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cluster);
      });

      it('should add only unique Cluster to an array', () => {
        const clusterArray: ICluster[] = [{ id: 123 }, { id: 456 }, { id: 27901 }];
        const clusterCollection: ICluster[] = [{ id: 123 }];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, ...clusterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cluster: ICluster = { id: 123 };
        const cluster2: ICluster = { id: 456 };
        expectedResult = service.addClusterToCollectionIfMissing([], cluster, cluster2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cluster);
        expect(expectedResult).toContain(cluster2);
      });

      it('should accept null and undefined values', () => {
        const cluster: ICluster = { id: 123 };
        expectedResult = service.addClusterToCollectionIfMissing([], null, cluster, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cluster);
      });

      it('should return initial array if no Cluster is added', () => {
        const clusterCollection: ICluster[] = [{ id: 123 }];
        expectedResult = service.addClusterToCollectionIfMissing(clusterCollection, undefined, null);
        expect(expectedResult).toEqual(clusterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
