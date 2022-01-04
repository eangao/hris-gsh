import { IEmployee } from 'app/entities/employee/employee.model';

export interface IDesignation {
  id?: number;
  name?: string;
  description?: string | null;
  employees?: IEmployee[] | null;
}

export class Designation implements IDesignation {
  constructor(public id?: number, public name?: string, public description?: string | null, public employees?: IEmployee[] | null) {}
}

export function getDesignationIdentifier(designation: IDesignation): number | undefined {
  return designation.id;
}
