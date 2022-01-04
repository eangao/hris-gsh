import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDependents, Dependents } from '../dependents.model';

import { DependentsService } from './dependents.service';

describe('Dependents Service', () => {
  let service: DependentsService;
  let httpMock: HttpTestingController;
  let elemDefault: IDependents;
  let expectedResult: IDependents | IDependents[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DependentsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      firstName: 'AAAAAAA',
      middleName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      relation: 'AAAAAAA',
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

    it('should create a Dependents', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Dependents()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dependents', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          relation: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dependents', () => {
      const patchObject = Object.assign({}, new Dependents());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dependents', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          relation: 'BBBBBB',
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

    it('should delete a Dependents', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDependentsToCollectionIfMissing', () => {
      it('should add a Dependents to an empty array', () => {
        const dependents: IDependents = { id: 123 };
        expectedResult = service.addDependentsToCollectionIfMissing([], dependents);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependents);
      });

      it('should not add a Dependents to an array that contains it', () => {
        const dependents: IDependents = { id: 123 };
        const dependentsCollection: IDependents[] = [
          {
            ...dependents,
          },
          { id: 456 },
        ];
        expectedResult = service.addDependentsToCollectionIfMissing(dependentsCollection, dependents);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dependents to an array that doesn't contain it", () => {
        const dependents: IDependents = { id: 123 };
        const dependentsCollection: IDependents[] = [{ id: 456 }];
        expectedResult = service.addDependentsToCollectionIfMissing(dependentsCollection, dependents);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependents);
      });

      it('should add only unique Dependents to an array', () => {
        const dependentsArray: IDependents[] = [{ id: 123 }, { id: 456 }, { id: 11602 }];
        const dependentsCollection: IDependents[] = [{ id: 123 }];
        expectedResult = service.addDependentsToCollectionIfMissing(dependentsCollection, ...dependentsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dependents: IDependents = { id: 123 };
        const dependents2: IDependents = { id: 456 };
        expectedResult = service.addDependentsToCollectionIfMissing([], dependents, dependents2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependents);
        expect(expectedResult).toContain(dependents2);
      });

      it('should accept null and undefined values', () => {
        const dependents: IDependents = { id: 123 };
        expectedResult = service.addDependentsToCollectionIfMissing([], null, dependents, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependents);
      });

      it('should return initial array if no Dependents is added', () => {
        const dependentsCollection: IDependents[] = [{ id: 123 }];
        expectedResult = service.addDependentsToCollectionIfMissing(dependentsCollection, undefined, null);
        expect(expectedResult).toEqual(dependentsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
