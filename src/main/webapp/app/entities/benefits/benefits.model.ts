import { IEmployee } from 'app/entities/employee/employee.model';

export interface IBenefits {
  id?: number;
  name?: string;
  employees?: IEmployee[] | null;
}

export class Benefits implements IBenefits {
  constructor(public id?: number, public name?: string, public employees?: IEmployee[] | null) {}
}

export function getBenefitsIdentifier(benefits: IBenefits): number | undefined {
  return benefits.id;
}
