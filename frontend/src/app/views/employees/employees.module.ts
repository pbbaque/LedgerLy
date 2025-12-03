import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { EmployeesRoutingModule } from './employees-routing.module';
import { EmployeeFormComponent } from './employee-form/employee-form.component';
import { ComponentsModule } from '../../components/components.module';



@NgModule({
  declarations: [
    EmployeeListComponent,
    EmployeeFormComponent
  ],
  imports: [
    CommonModule,
    ComponentsModule,
    EmployeesRoutingModule
  ]
})
export class EmployeesModule { }
