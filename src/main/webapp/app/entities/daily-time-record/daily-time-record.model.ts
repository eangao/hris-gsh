import { IEmployee } from 'app/entities/employee/employee.model';

export interface IDailyTimeRecord {
  id?: number;
  employeeBiometricId?: number | null;
  inputType?: string | null;
  attendanceType?: string | null;
  temperature?: string | null;
  logDate?: string | null;
  logTime?: string | null;
  employee?: IEmployee;
}

export class DailyTimeRecord implements IDailyTimeRecord {
  constructor(
    public id?: number,
    public employeeBiometricId?: number | null,
    public inputType?: string | null,
    public attendanceType?: string | null,
    public temperature?: string | null,
    public logDate?: string | null,
    public logTime?: string | null,
    public employee?: IEmployee
  ) {}
}

export function getDailyTimeRecordIdentifier(dailyTimeRecord: IDailyTimeRecord): number | undefined {
  return dailyTimeRecord.id;
}
