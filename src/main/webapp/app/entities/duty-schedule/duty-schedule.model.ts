import * as dayjs from 'dayjs';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IDutySchedule {
  id?: number;
  dateTimeIn?: dayjs.Dayjs | null;
  dateTimeOut?: dayjs.Dayjs | null;
  employee?: IEmployee;
}

export class DutySchedule implements IDutySchedule {
  constructor(
    public id?: number,
    public dateTimeIn?: dayjs.Dayjs | null,
    public dateTimeOut?: dayjs.Dayjs | null,
    public employee?: IEmployee
  ) {}
}

export function getDutyScheduleIdentifier(dutySchedule: IDutySchedule): number | undefined {
  return dutySchedule.id;
}
