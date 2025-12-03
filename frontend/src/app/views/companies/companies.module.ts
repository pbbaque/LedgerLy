import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompanyListComponent } from './company-list/company-list.component';
import { CompaniesRoutingModule } from './companies-routing.module';
import { ComponentsModule } from '../../components/components.module';
import { CompanyFormComponent } from './company-form/company-form.component';



@NgModule({
  declarations: [
    CompanyListComponent,
    CompanyFormComponent
  ],
  imports: [
    CommonModule,
    CompaniesRoutingModule,
    ComponentsModule
  ]
})
export class CompaniesModule { }
