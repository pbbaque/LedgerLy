import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const allowedRoles = route.data['roles'] as string[];

  if (!authService.isLoggedIn()) {
    router.navigate(['/auth/login']);
    return false;
  }

  const normalizeRole = (role: string): string => {
    switch (role) {
      case 'ROLE_SUPER_ADMIN': return 'superadmin';
      case 'ROLE_ADMIN': return 'admin';
      case 'ROLE_COMPANY_SUPER_ADMIN': return 'superadmincompany';
      case 'ROLE_COMPANY_ADMIN': return 'admincompany';
      case 'ROLE_USER': return 'user';
      default: return role.toLowerCase();
    }
  };

  const userRoles = authService.getRoles().map(normalizeRole);
  const hasAccess = allowedRoles.some(role => userRoles.includes(role));

  if (hasAccess)
    return true;

  router.navigate(['/dashboard']);
  return false;
};
