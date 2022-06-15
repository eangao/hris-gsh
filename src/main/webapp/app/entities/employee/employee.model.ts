import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IDutySchedule } from 'app/entities/duty-schedule/duty-schedule.model';
import { IDailyTimeRecord } from 'app/entities/daily-time-record/daily-time-record.model';
import { IDependents } from 'app/entities/dependents/dependents.model';
import { IEducation } from 'app/entities/education/education.model';
import { ITrainingHistory } from 'app/entities/training-history/training-history.model';
import { ILeave } from 'app/entities/leave/leave.model';
import { IDesignation } from 'app/entities/designation/designation.model';
import { IBenefits } from 'app/entities/benefits/benefits.model';
import { IDepartment } from 'app/entities/department/department.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { Status } from 'app/entities/enumerations/status.model';
import { EmploymentType } from 'app/entities/enumerations/employment-type.model';

export interface IEmployee {
  id?: number;
  employeeBiometricId?: number;
  username?: string;
  email?: string | null;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  nameSuffix?: string | null;
  birthdate?: dayjs.Dayjs;
  gender?: Gender;
  status?: Status;
  employmentType?: EmploymentType;
  mobileNumber?: string | null;
  dateHired?: dayjs.Dayjs | null;
  dateDeno?: dayjs.Dayjs | null;
  sickLeaveYearlyCredit?: number | null;
  sickLeaveYearlyCreditUsed?: number | null;
  leaveYearlyCredit?: number | null;
  leaveYearlyCreditUsed?: number | null;
  profileImageContentType?: string | null;
  profileImage?: string | null;
  presentAddressStreet?: string | null;
  presentAddressCity?: string | null;
  presentAddressProvince?: string | null;
  presentAddressZipcode?: number | null;
  homeAddressStreet?: string | null;
  homeAddressCity?: string | null;
  homeAddressProvince?: string | null;
  homeAddressZipcode?: number | null;
  user?: IUser;
  dutySchedules?: IDutySchedule[] | null;
  dailyTimeRecords?: IDailyTimeRecord[] | null;
  dependents?: IDependents[] | null;
  educations?: IEducation[] | null;
  trainingHistories?: ITrainingHistory[] | null;
  leaves?: ILeave[] | null;
  designations?: IDesignation[];
  benefits?: IBenefits[] | null;
  department?: IDepartment;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public employeeBiometricId?: number,
    public username?: string,
    public email?: string | null,
    public firstName?: string,
    public middleName?: string | null,
    public lastName?: string,
    public nameSuffix?: string | null,
    public birthdate?: dayjs.Dayjs,
    public gender?: Gender,
    public status?: Status,
    public employmentType?: EmploymentType,
    public mobileNumber?: string | null,
    public dateHired?: dayjs.Dayjs | null,
    public dateDeno?: dayjs.Dayjs | null,
    public sickLeaveYearlyCredit?: number | null,
    public sickLeaveYearlyCreditUsed?: number | null,
    public leaveYearlyCredit?: number | null,
    public leaveYearlyCreditUsed?: number | null,
    public profileImageContentType?: string | null,
    public profileImage?: string | null,
    public presentAddressStreet?: string | null,
    public presentAddressCity?: string | null,
    public presentAddressProvince?: string | null,
    public presentAddressZipcode?: number | null,
    public homeAddressStreet?: string | null,
    public homeAddressCity?: string | null,
    public homeAddressProvince?: string | null,
    public homeAddressZipcode?: number | null,
    public user?: IUser,
    public dutySchedules?: IDutySchedule[] | null,
    public dailyTimeRecords?: IDailyTimeRecord[] | null,
    public dependents?: IDependents[] | null,
    public educations?: IEducation[] | null,
    public trainingHistories?: ITrainingHistory[] | null,
    public leaves?: ILeave[] | null,
    public designations?: IDesignation[],
    public benefits?: IBenefits[] | null,
    public department?: IDepartment
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
