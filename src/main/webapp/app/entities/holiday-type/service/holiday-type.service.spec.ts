import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHolidayType, HolidayType } from '../holiday-type.model';

import { HolidayTypeService } from './holiday-type.service';

describe('HolidayType Service', () => {
  let service: HolidayTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IHolidayType;
  let expectedResult: IHolidayType | IHolidayType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HolidayTypeService);
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

    it('should create a HolidayType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new HolidayType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HolidayType', () => {
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

    it('should partial update a HolidayType', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new HolidayType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HolidayType', () => {
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

    it('should delete a HolidayType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHolidayTypeToCollectionIfMissing', () => {
      it('should add a HolidayType to an empty array', () => {
        const holidayType: IHolidayType = { id: 123 };
        expectedResult = service.addHolidayTypeToCollectionIfMissing([], holidayType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holidayType);
      });

      it('should not add a HolidayType to an array that contains it', () => {
        const holidayType: IHolidayType = { id: 123 };
        const holidayTypeCollection: IHolidayType[] = [
          {
            ...holidayType,
          },
          { id: 456 },
        ];
        expectedResult = service.addHolidayTypeToCollectionIfMissing(holidayTypeCollection, holidayType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HolidayType to an array that doesn't contain it", () => {
        const holidayType: IHolidayType = { id: 123 };
        const holidayTypeCollection: IHolidayType[] = [{ id: 456 }];
        expectedResult = service.addHolidayTypeToCollectionIfMissing(holidayTypeCollection, holidayType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holidayType);
      });

      it('should add only unique HolidayType to an array', () => {
        const holidayTypeArray: IHolidayType[] = [{ id: 123 }, { id: 456 }, { id: 91627 }];
        const holidayTypeCollection: IHolidayType[] = [{ id: 123 }];
        expectedResult = service.addHolidayTypeToCollectionIfMissing(holidayTypeCollection, ...holidayTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const holidayType: IHolidayType = { id: 123 };
        const holidayType2: IHolidayType = { id: 456 };
        expectedResult = service.addHolidayTypeToCollectionIfMissing([], holidayType, holidayType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(holidayType);
        expect(expectedResult).toContain(holidayType2);
      });

      it('should accept null and undefined values', () => {
        const holidayType: IHolidayType = { id: 123 };
        expectedResult = service.addHolidayTypeToCollectionIfMissing([], null, holidayType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(holidayType);
      });

      it('should return initial array if no HolidayType is added', () => {
        const holidayTypeCollection: IHolidayType[] = [{ id: 123 }];
        expectedResult = service.addHolidayTypeToCollectionIfMissing(holidayTypeCollection, undefined, null);
        expect(expectedResult).toEqual(holidayTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
