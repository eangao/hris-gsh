import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEducation, Education } from '../education.model';

import { EducationService } from './education.service';

describe('Education Service', () => {
  let service: EducationService;
  let httpMock: HttpTestingController;
  let elemDefault: IEducation;
  let expectedResult: IEducation | IEducation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EducationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bachelorDegree: 'AAAAAAA',
      yearGraduated: 0,
      school: 'AAAAAAA',
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

    it('should create a Education', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Education()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Education', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bachelorDegree: 'BBBBBB',
          yearGraduated: 1,
          school: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Education', () => {
      const patchObject = Object.assign(
        {
          yearGraduated: 1,
          school: 'BBBBBB',
        },
        new Education()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Education', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bachelorDegree: 'BBBBBB',
          yearGraduated: 1,
          school: 'BBBBBB',
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

    it('should delete a Education', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEducationToCollectionIfMissing', () => {
      it('should add a Education to an empty array', () => {
        const education: IEducation = { id: 123 };
        expectedResult = service.addEducationToCollectionIfMissing([], education);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(education);
      });

      it('should not add a Education to an array that contains it', () => {
        const education: IEducation = { id: 123 };
        const educationCollection: IEducation[] = [
          {
            ...education,
          },
          { id: 456 },
        ];
        expectedResult = service.addEducationToCollectionIfMissing(educationCollection, education);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Education to an array that doesn't contain it", () => {
        const education: IEducation = { id: 123 };
        const educationCollection: IEducation[] = [{ id: 456 }];
        expectedResult = service.addEducationToCollectionIfMissing(educationCollection, education);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(education);
      });

      it('should add only unique Education to an array', () => {
        const educationArray: IEducation[] = [{ id: 123 }, { id: 456 }, { id: 74039 }];
        const educationCollection: IEducation[] = [{ id: 123 }];
        expectedResult = service.addEducationToCollectionIfMissing(educationCollection, ...educationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const education: IEducation = { id: 123 };
        const education2: IEducation = { id: 456 };
        expectedResult = service.addEducationToCollectionIfMissing([], education, education2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(education);
        expect(expectedResult).toContain(education2);
      });

      it('should accept null and undefined values', () => {
        const education: IEducation = { id: 123 };
        expectedResult = service.addEducationToCollectionIfMissing([], null, education, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(education);
      });

      it('should return initial array if no Education is added', () => {
        const educationCollection: IEducation[] = [{ id: 123 }];
        expectedResult = service.addEducationToCollectionIfMissing(educationCollection, undefined, null);
        expect(expectedResult).toEqual(educationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
