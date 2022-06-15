import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILeave, Leave } from '../leave.model';

import { LeaveService } from './leave.service';

describe('Leave Service', () => {
  let service: LeaveService;
  let httpMock: HttpTestingController;
  let elemDefault: ILeave;
  let expectedResult: ILeave | ILeave[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LeaveService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateApply: currentDate,
      dateStart: currentDate,
      dateEnd: currentDate,
      dateReturn: currentDate,
      checkupDate: currentDate,
      convalescingPeriod: 0,
      diagnosis: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateApply: currentDate.format(DATE_FORMAT),
          dateStart: currentDate.format(DATE_FORMAT),
          dateEnd: currentDate.format(DATE_FORMAT),
          dateReturn: currentDate.format(DATE_FORMAT),
          checkupDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Leave', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateApply: currentDate.format(DATE_FORMAT),
          dateStart: currentDate.format(DATE_FORMAT),
          dateEnd: currentDate.format(DATE_FORMAT),
          dateReturn: currentDate.format(DATE_FORMAT),
          checkupDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateApply: currentDate,
          dateStart: currentDate,
          dateEnd: currentDate,
          dateReturn: currentDate,
          checkupDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Leave()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Leave', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateApply: currentDate.format(DATE_FORMAT),
          dateStart: currentDate.format(DATE_FORMAT),
          dateEnd: currentDate.format(DATE_FORMAT),
          dateReturn: currentDate.format(DATE_FORMAT),
          checkupDate: currentDate.format(DATE_FORMAT),
          convalescingPeriod: 1,
          diagnosis: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateApply: currentDate,
          dateStart: currentDate,
          dateEnd: currentDate,
          dateReturn: currentDate,
          checkupDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Leave', () => {
      const patchObject = Object.assign(
        {
          dateApply: currentDate.format(DATE_FORMAT),
          dateEnd: currentDate.format(DATE_FORMAT),
          dateReturn: currentDate.format(DATE_FORMAT),
        },
        new Leave()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateApply: currentDate,
          dateStart: currentDate,
          dateEnd: currentDate,
          dateReturn: currentDate,
          checkupDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Leave', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateApply: currentDate.format(DATE_FORMAT),
          dateStart: currentDate.format(DATE_FORMAT),
          dateEnd: currentDate.format(DATE_FORMAT),
          dateReturn: currentDate.format(DATE_FORMAT),
          checkupDate: currentDate.format(DATE_FORMAT),
          convalescingPeriod: 1,
          diagnosis: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateApply: currentDate,
          dateStart: currentDate,
          dateEnd: currentDate,
          dateReturn: currentDate,
          checkupDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Leave', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLeaveToCollectionIfMissing', () => {
      it('should add a Leave to an empty array', () => {
        const leave: ILeave = { id: 123 };
        expectedResult = service.addLeaveToCollectionIfMissing([], leave);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leave);
      });

      it('should not add a Leave to an array that contains it', () => {
        const leave: ILeave = { id: 123 };
        const leaveCollection: ILeave[] = [
          {
            ...leave,
          },
          { id: 456 },
        ];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, leave);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Leave to an array that doesn't contain it", () => {
        const leave: ILeave = { id: 123 };
        const leaveCollection: ILeave[] = [{ id: 456 }];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, leave);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leave);
      });

      it('should add only unique Leave to an array', () => {
        const leaveArray: ILeave[] = [{ id: 123 }, { id: 456 }, { id: 76656 }];
        const leaveCollection: ILeave[] = [{ id: 123 }];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, ...leaveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const leave: ILeave = { id: 123 };
        const leave2: ILeave = { id: 456 };
        expectedResult = service.addLeaveToCollectionIfMissing([], leave, leave2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(leave);
        expect(expectedResult).toContain(leave2);
      });

      it('should accept null and undefined values', () => {
        const leave: ILeave = { id: 123 };
        expectedResult = service.addLeaveToCollectionIfMissing([], null, leave, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(leave);
      });

      it('should return initial array if no Leave is added', () => {
        const leaveCollection: ILeave[] = [{ id: 123 }];
        expectedResult = service.addLeaveToCollectionIfMissing(leaveCollection, undefined, null);
        expect(expectedResult).toEqual(leaveCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
