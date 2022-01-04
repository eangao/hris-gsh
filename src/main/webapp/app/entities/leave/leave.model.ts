import * as dayjs from 'dayjs';
import { IEmployee } from 'app/entities/employee/employee.model';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';

export interface ILeave {
  id?: number;
  dateApply?: dayjs.Dayjs | null;
  dateStart?: dayjs.Dayjs | null;
  dateEnd?: dayjs.Dayjs | null;
  dateReturn?: dayjs.Dayjs | null;
  checkupDate?: dayjs.Dayjs | null;
  convalescingPeriod?: number | null;
  diagnosis?: string | null;
  employee?: IEmployee;
  leaveType?: ILeaveType;
}

export class Leave implements ILeave {
  constructor(
    public id?: number,
    public dateApply?: dayjs.Dayjs | null,
    public dateStart?: dayjs.Dayjs | null,
    public dateEnd?: dayjs.Dayjs | null,
    public dateReturn?: dayjs.Dayjs | null,
    public checkupDate?: dayjs.Dayjs | null,
    public convalescingPeriod?: number | null,
    public diagnosis?: string | null,
    public employee?: IEmployee,
    public leaveType?: ILeaveType
  ) {}
}

export function getLeaveIdentifier(leave: ILeave): number | undefined {
  return leave.id;
}
