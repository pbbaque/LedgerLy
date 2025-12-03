import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InvoiceListComponent } from './invoice-list/invoice-list.component';
import { InvoiceFormComponent } from './invoice-form/invoice-form.component';
import { InvoiceDetailComponent } from './invoice-detail/invoice-detail.component';
import { InvoiceDetailPageComponent } from './invoice-detail-page/invoice-detail-page.component';

const routes: Routes = [
  { path: '', component: InvoiceListComponent },
  { path: 'create', component: InvoiceFormComponent },
  { path: 'edit/:id', component: InvoiceFormComponent },
  { path: 'detail/:id', component: InvoiceDetailComponent },
  { path: ':id', component: InvoiceDetailPageComponent }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InvoicesRoutingModule { }
