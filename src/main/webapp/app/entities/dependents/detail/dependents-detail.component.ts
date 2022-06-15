import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDependents } from '../dependents.model';

@Component({
  selector: 'jhi-dependents-detail',
  templateUrl: './dependents-detail.component.html',
})
export class DependentsDetailComponent implements OnInit {
  dependents: IDependents | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependents }) => {
      this.dependents = dependents;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
