import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICluster } from '../cluster.model';

@Component({
  selector: 'jhi-cluster-detail',
  templateUrl: './cluster-detail.component.html',
})
export class ClusterDetailComponent implements OnInit {
  cluster: ICluster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cluster }) => {
      this.cluster = cluster;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
