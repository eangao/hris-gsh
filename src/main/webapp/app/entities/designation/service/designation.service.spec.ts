import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDesignation, Designation } from '../designation.model';

import { DesignationService } from './designation.service';

describe('Designation Service', () => {
  let service: DesignationService;
  let httpMock: HttpTestingController;
  let elemDefault: IDesignation;
  let expectedResult: IDesignation | IDesignation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DesignationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a Designation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Designation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Designation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Designation', () => {
      const patchObject = Object.assign({}, new Designation());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Designation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a Designation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDesignationToCollectionIfMissing', () => {
      it('should add a Designation to an empty array', () => {
        const designation: IDesignation = { id: 123 };
        expectedResult = service.addDesignationToCollectionIfMissing([], designation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(designation);
      });

      it('should not add a Designation to an array that contains it', () => {
        const designation: IDesignation = { id: 123 };
        const designationCollection: IDesignation[] = [
          {
            ...designation,
          },
          { id: 456 },
        ];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, designation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Designation to an array that doesn't contain it", () => {
        const designation: IDesignation = { id: 123 };
        const designationCollection: IDesignation[] = [{ id: 456 }];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, designation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(designation);
      });

      it('should add only unique Designation to an array', () => {
        const designationArray: IDesignation[] = [{ id: 123 }, { id: 456 }, { id: 5127 }];
        const designationCollection: IDesignation[] = [{ id: 123 }];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, ...designationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const designation: IDesignation = { id: 123 };
        const designation2: IDesignation = { id: 456 };
        expectedResult = service.addDesignationToCollectionIfMissing([], designation, designation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(designation);
        expect(expectedResult).toContain(designation2);
      });

      it('should accept null and undefined values', () => {
        const designation: IDesignation = { id: 123 };
        expectedResult = service.addDesignationToCollectionIfMissing([], null, designation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(designation);
      });

      it('should return initial array if no Designation is added', () => {
        const designationCollection: IDesignation[] = [{ id: 123 }];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, undefined, null);
        expect(expectedResult).toEqual(designationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
