import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { Status } from 'app/entities/enumerations/status.model';
import { EmploymentType } from 'app/entities/enumerations/employment-type.model';
import { IEmployee, Employee } from '../employee.model';

import { EmployeeService } from './employee.service';

describe('Employee Service', () => {
  let service: EmployeeService;
  let httpMock: HttpTestingController;
  let elemDefault: IEmployee;
  let expectedResult: IEmployee | IEmployee[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      employeeBiometricId: 0,
      username: 'AAAAAAA',
      email: 'AAAAAAA',
      firstName: 'AAAAAAA',
      middleName: 'AAAAAAA',
      lastName: 'AAAAAAA',
      nameSuffix: 'AAAAAAA',
      birthdate: currentDate,
      gender: Gender.MALE,
      status: Status.SINGLE,
      employmentType: EmploymentType.DINOMINATIONAL,
      mobileNumber: 'AAAAAAA',
      dateHired: currentDate,
      dateDeno: currentDate,
      sickLeaveYearlyCredit: 0,
      sickLeaveYearlyCreditUsed: 0,
      leaveYearlyCredit: 0,
      leaveYearlyCreditUsed: 0,
      profileImageContentType: 'image/png',
      profileImage: 'AAAAAAA',
      presentAddressStreet: 'AAAAAAA',
      presentAddressCity: 'AAAAAAA',
      presentAddressProvince: 'AAAAAAA',
      presentAddressZipcode: 0,
      homeAddressStreet: 'AAAAAAA',
      homeAddressCity: 'AAAAAAA',
      homeAddressProvince: 'AAAAAAA',
      homeAddressZipcode: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          birthdate: currentDate.format(DATE_FORMAT),
          dateHired: currentDate.format(DATE_FORMAT),
          dateDeno: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Employee', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          birthdate: currentDate.format(DATE_FORMAT),
          dateHired: currentDate.format(DATE_FORMAT),
          dateDeno: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          birthdate: currentDate,
          dateHired: currentDate,
          dateDeno: currentDate,
        },
        returnedFromService
      );

      service.create(new Employee()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Employee', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeBiometricId: 1,
          username: 'BBBBBB',
          email: 'BBBBBB',
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          nameSuffix: 'BBBBBB',
          birthdate: currentDate.format(DATE_FORMAT),
          gender: 'BBBBBB',
          status: 'BBBBBB',
          employmentType: 'BBBBBB',
          mobileNumber: 'BBBBBB',
          dateHired: currentDate.format(DATE_FORMAT),
          dateDeno: currentDate.format(DATE_FORMAT),
          sickLeaveYearlyCredit: 1,
          sickLeaveYearlyCreditUsed: 1,
          leaveYearlyCredit: 1,
          leaveYearlyCreditUsed: 1,
          profileImage: 'BBBBBB',
          presentAddressStreet: 'BBBBBB',
          presentAddressCity: 'BBBBBB',
          presentAddressProvince: 'BBBBBB',
          presentAddressZipcode: 1,
          homeAddressStreet: 'BBBBBB',
          homeAddressCity: 'BBBBBB',
          homeAddressProvince: 'BBBBBB',
          homeAddressZipcode: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          birthdate: currentDate,
          dateHired: currentDate,
          dateDeno: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Employee', () => {
      const patchObject = Object.assign(
        {
          username: 'BBBBBB',
          email: 'BBBBBB',
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          nameSuffix: 'BBBBBB',
          birthdate: currentDate.format(DATE_FORMAT),
          gender: 'BBBBBB',
          status: 'BBBBBB',
          employmentType: 'BBBBBB',
          mobileNumber: 'BBBBBB',
          dateHired: currentDate.format(DATE_FORMAT),
          dateDeno: currentDate.format(DATE_FORMAT),
          sickLeaveYearlyCredit: 1,
          leaveYearlyCredit: 1,
          leaveYearlyCreditUsed: 1,
          profileImage: 'BBBBBB',
          presentAddressStreet: 'BBBBBB',
          presentAddressProvince: 'BBBBBB',
          homeAddressStreet: 'BBBBBB',
          homeAddressCity: 'BBBBBB',
          homeAddressZipcode: 1,
        },
        new Employee()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          birthdate: currentDate,
          dateHired: currentDate,
          dateDeno: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Employee', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          employeeBiometricId: 1,
          username: 'BBBBBB',
          email: 'BBBBBB',
          firstName: 'BBBBBB',
          middleName: 'BBBBBB',
          lastName: 'BBBBBB',
          nameSuffix: 'BBBBBB',
          birthdate: currentDate.format(DATE_FORMAT),
          gender: 'BBBBBB',
          status: 'BBBBBB',
          employmentType: 'BBBBBB',
          mobileNumber: 'BBBBBB',
          dateHired: currentDate.format(DATE_FORMAT),
          dateDeno: currentDate.format(DATE_FORMAT),
          sickLeaveYearlyCredit: 1,
          sickLeaveYearlyCreditUsed: 1,
          leaveYearlyCredit: 1,
          leaveYearlyCreditUsed: 1,
          profileImage: 'BBBBBB',
          presentAddressStreet: 'BBBBBB',
          presentAddressCity: 'BBBBBB',
          presentAddressProvince: 'BBBBBB',
          presentAddressZipcode: 1,
          homeAddressStreet: 'BBBBBB',
          homeAddressCity: 'BBBBBB',
          homeAddressProvince: 'BBBBBB',
          homeAddressZipcode: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          birthdate: currentDate,
          dateHired: currentDate,
          dateDeno: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Employee', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmployeeToCollectionIfMissing', () => {
      it('should add a Employee to an empty array', () => {
        const employee: IEmployee = { id: 123 };
        expectedResult = service.addEmployeeToCollectionIfMissing([], employee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employee);
      });

      it('should not add a Employee to an array that contains it', () => {
        const employee: IEmployee = { id: 123 };
        const employeeCollection: IEmployee[] = [
          {
            ...employee,
          },
          { id: 456 },
        ];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, employee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Employee to an array that doesn't contain it", () => {
        const employee: IEmployee = { id: 123 };
        const employeeCollection: IEmployee[] = [{ id: 456 }];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, employee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employee);
      });

      it('should add only unique Employee to an array', () => {
        const employeeArray: IEmployee[] = [{ id: 123 }, { id: 456 }, { id: 6200 }];
        const employeeCollection: IEmployee[] = [{ id: 123 }];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, ...employeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employee: IEmployee = { id: 123 };
        const employee2: IEmployee = { id: 456 };
        expectedResult = service.addEmployeeToCollectionIfMissing([], employee, employee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employee);
        expect(expectedResult).toContain(employee2);
      });

      it('should accept null and undefined values', () => {
        const employee: IEmployee = { id: 123 };
        expectedResult = service.addEmployeeToCollectionIfMissing([], null, employee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employee);
      });

      it('should return initial array if no Employee is added', () => {
        const employeeCollection: IEmployee[] = [{ id: 123 }];
        expectedResult = service.addEmployeeToCollectionIfMissing(employeeCollection, undefined, null);
        expect(expectedResult).toEqual(employeeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
