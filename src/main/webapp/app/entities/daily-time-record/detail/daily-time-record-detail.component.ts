import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDailyTimeRecord } from '../daily-time-record.model';

@Component({
  selector: 'jhi-daily-time-record-detail',
  templateUrl: './daily-time-record-detail.component.html',
})
export class DailyTimeRecordDetailComponent implements OnInit {
  dailyTimeRecord: IDailyTimeRecord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyTimeRecord }) => {
      this.dailyTimeRecord = dailyTimeRecord;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
