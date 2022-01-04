import * as dayjs from 'dayjs';

export interface IHoliday {
  id?: number;
  name?: string;
  date?: dayjs.Dayjs;
  description?: string | null;
}

export class Holiday implements IHoliday {
  constructor(public id?: number, public name?: string, public date?: dayjs.Dayjs, public description?: string | null) {}
}

export function getHolidayIdentifier(holiday: IHoliday): number | undefined {
  return holiday.id;
}
