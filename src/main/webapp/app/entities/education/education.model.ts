import { IEmployee } from 'app/entities/employee/employee.model';

export interface IEducation {
  id?: number;
  bachelorDegree?: string;
  yearGraduated?: number;
  school?: string;
  employee?: IEmployee;
}

export class Education implements IEducation {
  constructor(
    public id?: number,
    public bachelorDegree?: string,
    public yearGraduated?: number,
    public school?: string,
    public employee?: IEmployee
  ) {}
}

export function getEducationIdentifier(education: IEducation): number | undefined {
  return education.id;
}
