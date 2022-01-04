import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDutySchedule, DutySchedule } from '../duty-schedule.model';

import { DutyScheduleService } from './duty-schedule.service';

describe('DutySchedule Service', () => {
  let service: DutyScheduleService;
  let httpMock: HttpTestingController;
  let elemDefault: IDutySchedule;
  let expectedResult: IDutySchedule | IDutySchedule[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DutyScheduleService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateTimeIn: currentDate,
      dateTimeOut: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateTimeIn: currentDate.format(DATE_TIME_FORMAT),
          dateTimeOut: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DutySchedule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateTimeIn: currentDate.format(DATE_TIME_FORMAT),
          dateTimeOut: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateTimeIn: currentDate,
          dateTimeOut: currentDate,
        },
        returnedFromService
      );

      service.create(new DutySchedule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DutySchedule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateTimeIn: currentDate.format(DATE_TIME_FORMAT),
          dateTimeOut: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateTimeIn: currentDate,
          dateTimeOut: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DutySchedule', () => {
      const patchObject = Object.assign(
        {
          dateTimeOut: currentDate.format(DATE_TIME_FORMAT),
        },
        new DutySchedule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateTimeIn: currentDate,
          dateTimeOut: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DutySchedule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateTimeIn: currentDate.format(DATE_TIME_FORMAT),
          dateTimeOut: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateTimeIn: currentDate,
          dateTimeOut: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DutySchedule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDutyScheduleToCollectionIfMissing', () => {
      it('should add a DutySchedule to an empty array', () => {
        const dutySchedule: IDutySchedule = { id: 123 };
        expectedResult = service.addDutyScheduleToCollectionIfMissing([], dutySchedule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dutySchedule);
      });

      it('should not add a DutySchedule to an array that contains it', () => {
        const dutySchedule: IDutySchedule = { id: 123 };
        const dutyScheduleCollection: IDutySchedule[] = [
          {
            ...dutySchedule,
          },
          { id: 456 },
        ];
        expectedResult = service.addDutyScheduleToCollectionIfMissing(dutyScheduleCollection, dutySchedule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DutySchedule to an array that doesn't contain it", () => {
        const dutySchedule: IDutySchedule = { id: 123 };
        const dutyScheduleCollection: IDutySchedule[] = [{ id: 456 }];
        expectedResult = service.addDutyScheduleToCollectionIfMissing(dutyScheduleCollection, dutySchedule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dutySchedule);
      });

      it('should add only unique DutySchedule to an array', () => {
        const dutyScheduleArray: IDutySchedule[] = [{ id: 123 }, { id: 456 }, { id: 86365 }];
        const dutyScheduleCollection: IDutySchedule[] = [{ id: 123 }];
        expectedResult = service.addDutyScheduleToCollectionIfMissing(dutyScheduleCollection, ...dutyScheduleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dutySchedule: IDutySchedule = { id: 123 };
        const dutySchedule2: IDutySchedule = { id: 456 };
        expectedResult = service.addDutyScheduleToCollectionIfMissing([], dutySchedule, dutySchedule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dutySchedule);
        expect(expectedResult).toContain(dutySchedule2);
      });

      it('should accept null and undefined values', () => {
        const dutySchedule: IDutySchedule = { id: 123 };
        expectedResult = service.addDutyScheduleToCollectionIfMissing([], null, dutySchedule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dutySchedule);
      });

      it('should return initial array if no DutySchedule is added', () => {
        const dutyScheduleCollection: IDutySchedule[] = [{ id: 123 }];
        expectedResult = service.addDutyScheduleToCollectionIfMissing(dutyScheduleCollection, undefined, null);
        expect(expectedResult).toEqual(dutyScheduleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
