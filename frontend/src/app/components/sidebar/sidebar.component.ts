import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  standalone: false
})
export class SidebarComponent {

  sidebarOpen = false;

  constructor(private authService: AuthService) {}

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar() {
    if (this.sidebarOpen) {
      this.toggleSidebar();
    }
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }
}
