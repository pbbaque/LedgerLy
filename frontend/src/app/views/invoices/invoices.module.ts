import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InvoiceListComponent } from './invoice-list/invoice-list.component';
import { InvoicesRoutingModule } from './invoices-routing.module';
import { InvoiceFormComponent } from './invoice-form/invoice-form.component';
import { InvoiceDetailComponent } from './invoice-detail/invoice-detail.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ComponentsModule } from '../../components/components.module';
import { InvoiceCardComponent } from './invoice-card/invoice-card.component';
import { InvoiceDetailPageComponent } from './invoice-detail-page/invoice-detail-page.component';



@NgModule({
  declarations: [
    InvoiceListComponent,
    InvoiceFormComponent,
    InvoiceDetailComponent,
    InvoiceCardComponent,
    InvoiceDetailPageComponent
  ],
  imports: [
    CommonModule,
    InvoicesRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ComponentsModule
  ]
})
export class InvoicesModule { }
