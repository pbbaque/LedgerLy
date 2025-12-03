import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { CompanyProfileComponent } from './company-profile/company-profile.component';


const routes: Routes = [
  { path: 'user', component: UserProfileComponent },
  { path: 'company', component: CompanyProfileComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfilesRoutingModule { }
