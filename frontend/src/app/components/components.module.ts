import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './alert/alert.component';
import { HeaderComponent } from './header/header.component';
import { PaginationComponent } from './pagination/pagination.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { FooterComponent } from './footer/footer.component';
import { RouterModule } from '@angular/router';
import { ConfirmComponent } from './confirm/confirm.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EntityDetailComponent } from './entity-detail/entity-detail.component';



@NgModule({
  declarations: [
    AlertComponent,
    HeaderComponent,
    PaginationComponent,
    SidebarComponent,
    FooterComponent,
    ConfirmComponent,
    EntityDetailComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    AlertComponent,
    HeaderComponent,
    PaginationComponent,
    SidebarComponent,
    FooterComponent,
    ConfirmComponent,
    FormsModule,
    ReactiveFormsModule,
    EntityDetailComponent
  ]
})
export class ComponentsModule { }
