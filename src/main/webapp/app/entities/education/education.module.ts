import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EducationComponent } from './list/education.component';
import { EducationDetailComponent } from './detail/education-detail.component';
import { EducationUpdateComponent } from './update/education-update.component';
import { EducationDeleteDialogComponent } from './delete/education-delete-dialog.component';
import { EducationRoutingModule } from './route/education-routing.module';

@NgModule({
  imports: [SharedModule, EducationRoutingModule],
  declarations: [EducationComponent, EducationDetailComponent, EducationUpdateComponent, EducationDeleteDialogComponent],
  entryComponents: [EducationDeleteDialogComponent],
})
export class EducationModule {}
