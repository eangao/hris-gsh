import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee',
        data: { pageTitle: 'hrisGshApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'cluster',
        data: { pageTitle: 'hrisGshApp.cluster.home.title' },
        loadChildren: () => import('./cluster/cluster.module').then(m => m.ClusterModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'hrisGshApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'designation',
        data: { pageTitle: 'hrisGshApp.designation.home.title' },
        loadChildren: () => import('./designation/designation.module').then(m => m.DesignationModule),
      },
      {
        path: 'duty-schedule',
        data: { pageTitle: 'hrisGshApp.dutySchedule.home.title' },
        loadChildren: () => import('./duty-schedule/duty-schedule.module').then(m => m.DutyScheduleModule),
      },
      {
        path: 'daily-time-record',
        data: { pageTitle: 'hrisGshApp.dailyTimeRecord.home.title' },
        loadChildren: () => import('./daily-time-record/daily-time-record.module').then(m => m.DailyTimeRecordModule),
      },
      {
        path: 'benefits',
        data: { pageTitle: 'hrisGshApp.benefits.home.title' },
        loadChildren: () => import('./benefits/benefits.module').then(m => m.BenefitsModule),
      },
      {
        path: 'dependents',
        data: { pageTitle: 'hrisGshApp.dependents.home.title' },
        loadChildren: () => import('./dependents/dependents.module').then(m => m.DependentsModule),
      },
      {
        path: 'education',
        data: { pageTitle: 'hrisGshApp.education.home.title' },
        loadChildren: () => import('./education/education.module').then(m => m.EducationModule),
      },
      {
        path: 'training-history',
        data: { pageTitle: 'hrisGshApp.trainingHistory.home.title' },
        loadChildren: () => import('./training-history/training-history.module').then(m => m.TrainingHistoryModule),
      },
      {
        path: 'leave',
        data: { pageTitle: 'hrisGshApp.leave.home.title' },
        loadChildren: () => import('./leave/leave.module').then(m => m.LeaveModule),
      },
      {
        path: 'leave-type',
        data: { pageTitle: 'hrisGshApp.leaveType.home.title' },
        loadChildren: () => import('./leave-type/leave-type.module').then(m => m.LeaveTypeModule),
      },
      {
        path: 'holiday',
        data: { pageTitle: 'hrisGshApp.holiday.home.title' },
        loadChildren: () => import('./holiday/holiday.module').then(m => m.HolidayModule),
      },
      {
        path: 'holiday-type',
        data: { pageTitle: 'hrisGshApp.holidayType.home.title' },
        loadChildren: () => import('./holiday-type/holiday-type.module').then(m => m.HolidayTypeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
