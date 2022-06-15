import { IEmployee } from 'app/entities/employee/employee.model';

export interface IDependents {
  id?: number;
  firstName?: string;
  middleName?: string | null;
  lastName?: string;
  relation?: string;
  employee?: IEmployee;
}

export class Dependents implements IDependents {
  constructor(
    public id?: number,
    public firstName?: string,
    public middleName?: string | null,
    public lastName?: string,
    public relation?: string,
    public employee?: IEmployee
  ) {}
}

export function getDependentsIdentifier(dependents: IDependents): number | undefined {
  return dependents.id;
}
