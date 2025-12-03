import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './views/not-found/not-found.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full'
  },
  { 
    path: 'auth', loadChildren: () => import('./views/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { 
        path: 'dashboard', loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule),
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany', 'user'] }
      },
      { 
        path: 'clients', loadChildren: () => import('./views/clients/clients.module').then(m => m.ClientsModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany', 'user'] }
      },
      { 
        path: 'companies', loadChildren: () => import('./views/companies/companies.module').then(m => m.CompaniesModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin'] }
      },
      { 
        path: 'employees', loadChildren: () => import('./views/employees/employees.module').then(m => m.EmployeesModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany'] }
      },
      { 
        path: 'invoices', loadChildren: () => import('./views/invoices/invoices.module').then(m => m.InvoicesModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany', 'user'] }
      },
      { 
        path: 'users', loadChildren: () => import('./views/users/users.module').then(m => m.UsersModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany'] }
      },
      { 
        path: 'products', loadChildren: () => import('./views/products/products.module').then(m => m.ProductsModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany', 'user'] }
      },
      { 
        path: 'profiles', loadChildren: () => import('./views/profiles/profiles.module').then(m => m.ProfilesModule), 
        canActivate: [roleGuard], data: { roles: ['superadmin', 'admin', 'superadmincompany', 'admincompany', 'user'] }
      },
      { path: '**', component: NotFoundComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
