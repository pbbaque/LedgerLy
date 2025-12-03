import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProfilesRoutingModule } from './profiles-routing.module';
import { ComponentsModule } from '../../components/components.module';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { CompanyProfileComponent } from './company-profile/company-profile.component';



@NgModule({
  declarations: [
    UserProfileComponent,
    CompanyProfileComponent
  ],
  imports: [
    CommonModule,
    ComponentsModule,
    ProfilesRoutingModule
  ]
})
export class ProfilesModule { }
