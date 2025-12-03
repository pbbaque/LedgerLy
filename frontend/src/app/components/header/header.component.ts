import { Component, HostListener } from '@angular/core';
import { Event, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
    standalone: false
})
export class HeaderComponent {
  
  dropdownOpen: boolean = false;
  showConfirmLogout: boolean = false;

  constructor( private authService: AuthService, private router: Router) {}

  onLogout() {
    this.showConfirmLogout = true;
  }

  confirmLogout() {
    this.showConfirmLogout = false;
    this.authService.logout();
    this.router.navigate(['auth/login']);
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }

  cancelLogout() {
    this.showConfirmLogout = false;
  }

  openUserProfile() {
    this.router.navigate(['profiles/user']);
  }

  openCompanyProfile() {
    this.router.navigate(['profiles/company']);
  }
}
