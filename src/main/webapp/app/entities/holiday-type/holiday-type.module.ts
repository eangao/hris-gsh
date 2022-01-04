import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HolidayTypeComponent } from './list/holiday-type.component';
import { HolidayTypeDetailComponent } from './detail/holiday-type-detail.component';
import { HolidayTypeUpdateComponent } from './update/holiday-type-update.component';
import { HolidayTypeDeleteDialogComponent } from './delete/holiday-type-delete-dialog.component';
import { HolidayTypeRoutingModule } from './route/holiday-type-routing.module';

@NgModule({
  imports: [SharedModule, HolidayTypeRoutingModule],
  declarations: [HolidayTypeComponent, HolidayTypeDetailComponent, HolidayTypeUpdateComponent, HolidayTypeDeleteDialogComponent],
  entryComponents: [HolidayTypeDeleteDialogComponent],
})
export class HolidayTypeModule {}
