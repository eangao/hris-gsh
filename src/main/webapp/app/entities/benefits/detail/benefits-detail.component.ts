import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBenefits } from '../benefits.model';

@Component({
  selector: 'jhi-benefits-detail',
  templateUrl: './benefits-detail.component.html',
})
export class BenefitsDetailComponent implements OnInit {
  benefits: IBenefits | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ benefits }) => {
      this.benefits = benefits;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
