import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductFormComponent } from './product-form/product-form.component';
import { ComponentsModule } from '../../components/components.module';

import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ProductCardComponent } from './product-card/product-card.component';



@NgModule({
    schemas: [ CUSTOM_ELEMENTS_SCHEMA],
  
  declarations: [
    ProductListComponent,
    ProductFormComponent,
    ProductCardComponent
  ],
  imports: [
    CommonModule,
    ComponentsModule,
    ProductsRoutingModule
  ]
})
export class ProductsModule { }
