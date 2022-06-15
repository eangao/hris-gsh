import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITrainingHistory, TrainingHistory } from '../training-history.model';

import { TrainingHistoryService } from './training-history.service';

describe('TrainingHistory Service', () => {
  let service: TrainingHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrainingHistory;
  let expectedResult: ITrainingHistory | ITrainingHistory[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrainingHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      date: currentDate,
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TrainingHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new TrainingHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrainingHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrainingHistory', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
        },
        new TrainingHistory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrainingHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          date: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TrainingHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrainingHistoryToCollectionIfMissing', () => {
      it('should add a TrainingHistory to an empty array', () => {
        const trainingHistory: ITrainingHistory = { id: 123 };
        expectedResult = service.addTrainingHistoryToCollectionIfMissing([], trainingHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingHistory);
      });

      it('should not add a TrainingHistory to an array that contains it', () => {
        const trainingHistory: ITrainingHistory = { id: 123 };
        const trainingHistoryCollection: ITrainingHistory[] = [
          {
            ...trainingHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, trainingHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrainingHistory to an array that doesn't contain it", () => {
        const trainingHistory: ITrainingHistory = { id: 123 };
        const trainingHistoryCollection: ITrainingHistory[] = [{ id: 456 }];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, trainingHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingHistory);
      });

      it('should add only unique TrainingHistory to an array', () => {
        const trainingHistoryArray: ITrainingHistory[] = [{ id: 123 }, { id: 456 }, { id: 18021 }];
        const trainingHistoryCollection: ITrainingHistory[] = [{ id: 123 }];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, ...trainingHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trainingHistory: ITrainingHistory = { id: 123 };
        const trainingHistory2: ITrainingHistory = { id: 456 };
        expectedResult = service.addTrainingHistoryToCollectionIfMissing([], trainingHistory, trainingHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainingHistory);
        expect(expectedResult).toContain(trainingHistory2);
      });

      it('should accept null and undefined values', () => {
        const trainingHistory: ITrainingHistory = { id: 123 };
        expectedResult = service.addTrainingHistoryToCollectionIfMissing([], null, trainingHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainingHistory);
      });

      it('should return initial array if no TrainingHistory is added', () => {
        const trainingHistoryCollection: ITrainingHistory[] = [{ id: 123 }];
        expectedResult = service.addTrainingHistoryToCollectionIfMissing(trainingHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(trainingHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
