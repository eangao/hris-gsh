import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBenefits, Benefits } from '../benefits.model';

import { BenefitsService } from './benefits.service';

describe('Benefits Service', () => {
  let service: BenefitsService;
  let httpMock: HttpTestingController;
  let elemDefault: IBenefits;
  let expectedResult: IBenefits | IBenefits[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BenefitsService);
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

    it('should create a Benefits', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Benefits()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Benefits', () => {
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

    it('should partial update a Benefits', () => {
      const patchObject = Object.assign({}, new Benefits());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Benefits', () => {
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

    it('should delete a Benefits', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBenefitsToCollectionIfMissing', () => {
      it('should add a Benefits to an empty array', () => {
        const benefits: IBenefits = { id: 123 };
        expectedResult = service.addBenefitsToCollectionIfMissing([], benefits);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(benefits);
      });

      it('should not add a Benefits to an array that contains it', () => {
        const benefits: IBenefits = { id: 123 };
        const benefitsCollection: IBenefits[] = [
          {
            ...benefits,
          },
          { id: 456 },
        ];
        expectedResult = service.addBenefitsToCollectionIfMissing(benefitsCollection, benefits);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Benefits to an array that doesn't contain it", () => {
        const benefits: IBenefits = { id: 123 };
        const benefitsCollection: IBenefits[] = [{ id: 456 }];
        expectedResult = service.addBenefitsToCollectionIfMissing(benefitsCollection, benefits);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(benefits);
      });

      it('should add only unique Benefits to an array', () => {
        const benefitsArray: IBenefits[] = [{ id: 123 }, { id: 456 }, { id: 70943 }];
        const benefitsCollection: IBenefits[] = [{ id: 123 }];
        expectedResult = service.addBenefitsToCollectionIfMissing(benefitsCollection, ...benefitsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const benefits: IBenefits = { id: 123 };
        const benefits2: IBenefits = { id: 456 };
        expectedResult = service.addBenefitsToCollectionIfMissing([], benefits, benefits2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(benefits);
        expect(expectedResult).toContain(benefits2);
      });

      it('should accept null and undefined values', () => {
        const benefits: IBenefits = { id: 123 };
        expectedResult = service.addBenefitsToCollectionIfMissing([], null, benefits, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(benefits);
      });

      it('should return initial array if no Benefits is added', () => {
        const benefitsCollection: IBenefits[] = [{ id: 123 }];
        expectedResult = service.addBenefitsToCollectionIfMissing(benefitsCollection, undefined, null);
        expect(expectedResult).toEqual(benefitsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
