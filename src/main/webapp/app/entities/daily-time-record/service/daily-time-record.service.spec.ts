import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDailyTimeRecord, DailyTimeRecord } from '../daily-time-record.model';

import { DailyTimeRecordService } from './daily-time-record.service';

describe('DailyTimeRecord Service', () => {
  let service: DailyTimeRecordService;
  let httpMock: HttpTestingController;
  let elemDefault: IDailyTimeRecord;
  let expectedResult: IDailyTimeRecord | IDailyTimeRecord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DailyTimeRecordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      employeeBiometricId: 0,
      inputType: 'AAAAAAA',
      attendanceType: 'AAAAAAA',
      temperature: 'AAAAAAA',
      logDate: 'AAAAAAA',
      logTime: 'AAAAAAA',
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

    it('should create a DailyTimeRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DailyTimeRecord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DailyTimeRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeBiometricId: 1,
          inputType: 'BBBBBB',
          attendanceType: 'BBBBBB',
          temperature: 'BBBBBB',
          logDate: 'BBBBBB',
          logTime: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DailyTimeRecord', () => {
      const patchObject = Object.assign(
        {
          employeeBiometricId: 1,
          inputType: 'BBBBBB',
          temperature: 'BBBBBB',
          logDate: 'BBBBBB',
        },
        new DailyTimeRecord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DailyTimeRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeBiometricId: 1,
          inputType: 'BBBBBB',
          attendanceType: 'BBBBBB',
          temperature: 'BBBBBB',
          logDate: 'BBBBBB',
          logTime: 'BBBBBB',
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

    it('should delete a DailyTimeRecord', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDailyTimeRecordToCollectionIfMissing', () => {
      it('should add a DailyTimeRecord to an empty array', () => {
        const dailyTimeRecord: IDailyTimeRecord = { id: 123 };
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing([], dailyTimeRecord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dailyTimeRecord);
      });

      it('should not add a DailyTimeRecord to an array that contains it', () => {
        const dailyTimeRecord: IDailyTimeRecord = { id: 123 };
        const dailyTimeRecordCollection: IDailyTimeRecord[] = [
          {
            ...dailyTimeRecord,
          },
          { id: 456 },
        ];
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing(dailyTimeRecordCollection, dailyTimeRecord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DailyTimeRecord to an array that doesn't contain it", () => {
        const dailyTimeRecord: IDailyTimeRecord = { id: 123 };
        const dailyTimeRecordCollection: IDailyTimeRecord[] = [{ id: 456 }];
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing(dailyTimeRecordCollection, dailyTimeRecord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dailyTimeRecord);
      });

      it('should add only unique DailyTimeRecord to an array', () => {
        const dailyTimeRecordArray: IDailyTimeRecord[] = [{ id: 123 }, { id: 456 }, { id: 66793 }];
        const dailyTimeRecordCollection: IDailyTimeRecord[] = [{ id: 123 }];
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing(dailyTimeRecordCollection, ...dailyTimeRecordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dailyTimeRecord: IDailyTimeRecord = { id: 123 };
        const dailyTimeRecord2: IDailyTimeRecord = { id: 456 };
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing([], dailyTimeRecord, dailyTimeRecord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dailyTimeRecord);
        expect(expectedResult).toContain(dailyTimeRecord2);
      });

      it('should accept null and undefined values', () => {
        const dailyTimeRecord: IDailyTimeRecord = { id: 123 };
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing([], null, dailyTimeRecord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dailyTimeRecord);
      });

      it('should return initial array if no DailyTimeRecord is added', () => {
        const dailyTimeRecordCollection: IDailyTimeRecord[] = [{ id: 123 }];
        expectedResult = service.addDailyTimeRecordToCollectionIfMissing(dailyTimeRecordCollection, undefined, null);
        expect(expectedResult).toEqual(dailyTimeRecordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
