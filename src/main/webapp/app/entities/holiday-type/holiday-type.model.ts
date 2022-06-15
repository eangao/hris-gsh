export interface IHolidayType {
  id?: number;
  name?: string;
}

export class HolidayType implements IHolidayType {
  constructor(public id?: number, public name?: string) {}
}

export function getHolidayTypeIdentifier(holidayType: IHolidayType): number | undefined {
  return holidayType.id;
}
