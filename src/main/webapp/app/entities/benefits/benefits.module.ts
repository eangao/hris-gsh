import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BenefitsComponent } from './list/benefits.component';
import { BenefitsDetailComponent } from './detail/benefits-detail.component';
import { BenefitsUpdateComponent } from './update/benefits-update.component';
import { BenefitsDeleteDialogComponent } from './delete/benefits-delete-dialog.component';
import { BenefitsRoutingModule } from './route/benefits-routing.module';

@NgModule({
  imports: [SharedModule, BenefitsRoutingModule],
  declarations: [BenefitsComponent, BenefitsDetailComponent, BenefitsUpdateComponent, BenefitsDeleteDialogComponent],
  entryComponents: [BenefitsDeleteDialogComponent],
})
export class BenefitsModule {}
