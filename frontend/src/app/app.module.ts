import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ComponentsModule } from './components/components.module';
import { NotFoundComponent } from './views/not-found/not-found.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { authInterceptor } from './interceptors/auth.interceptor';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';


@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA],
  declarations: [
    AppComponent,
    NotFoundComponent,
    MainLayoutComponent,
    AuthLayoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ComponentsModule
  ],
  providers: [ 
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
