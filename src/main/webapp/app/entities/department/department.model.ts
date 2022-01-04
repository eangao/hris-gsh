import { IEmployee } from 'app/entities/employee/employee.model';
import { ICluster } from 'app/entities/cluster/cluster.model';

export interface IDepartment {
  id?: number;
  name?: string;
  employees?: IEmployee[] | null;
  cluster?: ICluster;
}

export class Department implements IDepartment {
  constructor(public id?: number, public name?: string, public employees?: IEmployee[] | null, public cluster?: ICluster) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
