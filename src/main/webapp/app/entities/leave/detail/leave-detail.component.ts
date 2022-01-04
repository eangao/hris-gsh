import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeave } from '../leave.model';

@Component({
  selector: 'jhi-leave-detail',
  templateUrl: './leave-detail.component.html',
})
export class LeaveDetailComponent implements OnInit {
  leave: ILeave | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leave }) => {
      this.leave = leave;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
