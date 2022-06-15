import { ILeave } from 'app/entities/leave/leave.model';

export interface ILeaveType {
  id?: number;
  name?: string;
  description?: string | null;
  leaves?: ILeave[] | null;
}

export class LeaveType implements ILeaveType {
  constructor(public id?: number, public name?: string, public description?: string | null, public leaves?: ILeave[] | null) {}
}

export function getLeaveTypeIdentifier(leaveType: ILeaveType): number | undefined {
  return leaveType.id;
}
