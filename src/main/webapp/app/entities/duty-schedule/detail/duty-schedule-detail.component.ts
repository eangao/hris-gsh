import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDutySchedule } from '../duty-schedule.model';

@Component({
  selector: 'jhi-duty-schedule-detail',
  templateUrl: './duty-schedule-detail.component.html',
})
export class DutyScheduleDetailComponent implements OnInit {
  dutySchedule: IDutySchedule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dutySchedule }) => {
      this.dutySchedule = dutySchedule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
