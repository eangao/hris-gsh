import * as dayjs from 'dayjs';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface ITrainingHistory {
  id?: number;
  name?: string;
  date?: dayjs.Dayjs;
  description?: string | null;
  employee?: IEmployee;
}

export class TrainingHistory implements ITrainingHistory {
  constructor(
    public id?: number,
    public name?: string,
    public date?: dayjs.Dayjs,
    public description?: string | null,
    public employee?: IEmployee
  ) {}
}

export function getTrainingHistoryIdentifier(trainingHistory: ITrainingHistory): number | undefined {
  return trainingHistory.id;
}
