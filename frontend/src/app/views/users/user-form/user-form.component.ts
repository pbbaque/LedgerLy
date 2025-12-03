import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../services/user.service';
import { CompanyService } from '../../../services/company.service';
import { EmployeeService } from '../../../services/employee.service';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user';
import { Company } from '../../../models/company';
import { Employee } from '../../../models/employee';
import { Role } from '../../../models/role';

@Component({
    selector: 'app-user-form',
    templateUrl: './user-form.component.html',
    styleUrl: './user-form.component.scss',
    standalone: false
})
export class UserFormComponent implements OnInit {
  errorMessage: string = '';
  errorVisible: boolean = false;
  userForm!: FormGroup;
  isEditMode: boolean = false;
  userId: number | null = null;
  roles: Role[] = [];
  rolesIds: number[] = [];
  availableRoles: Role[] = [];
  isAdmin: boolean = false;
  isCompanyRole: boolean = false;

  private roleLabels: Record<string, string> = {
    'ROLE_SUPER_ADMIN': 'Super Administrador',
    'ROLE_ADMIN': 'Administrador',
    'ROLE_COMPANY_SUPER_ADMIN': 'Super Administrador de Empresa',
    'ROLE_COMPANY_ADMIN': 'Administrador de Empresa',
    'ROLE_USER': 'Usuario'
  };

  private roleOrder: string[] = [
    'ROLE_SUPER_ADMIN',
    'ROLE_ADMIN',
    'ROLE_COMPANY_SUPER_ADMIN',
    'ROLE_COMPANY_ADMIN',
    'ROLE_USER'
  ];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.isAdmin =
      this.authService.hasRole('ROLE_ADMIN') ||
      this.authService.hasRole('ROLE_SUPER_ADMIN');

    this.isCompanyRole =
      this.authService.hasRole('ROLE_COMPANY_ADMIN') ||
      this.authService.hasRole('ROLE_COMPANY_SUPER_ADMIN');

    this.userForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      repeat: [''],
      company: this.fb.group({
        id: [null],
        name: ['']
      }),
      employee: this.fb.group({
        id: [null],
        name: [''],
        lastname: [''],
        email: ['']
      })
    }, { validators: this.passwordsMatchValidator });

    this.loadRoles();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.userId = +id;
        this.loadUser(this.userId);
      }
    });

    if (!this.isEditMode) {
      this.password?.setValidators([Validators.required]);
      this.repeat?.setValidators([Validators.required]);
      this.password?.updateValueAndValidity();
      this.repeat?.updateValueAndValidity();
    }
  }

  getRoleLabel(roleName: string): string {
    return this.roleLabels[roleName] || roleName;
  }

  loadUser(id: number): void {
    this.userService.findById(id).subscribe({
      next: (user: User) => {
        this.userForm.patchValue({
          username: user.username,
          email: user.email,
        });

        this.roles = user.roles || [];
        this.rolesIds = this.roles.map(r => r.id);
        if (user.employee && user.employee.company) {
          const company = user.employee.company;
          const employee = user.employee;

          this.userForm.patchValue({
            company,
            employee
          });
        } else {
          this.userForm.patchValue({
            company: null,
            employee: null
          });
        }
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar el usuario';
        this.errorVisible = true;
      }
    });
  }

  loadRoles(): void {
    this.userService.findAllRoles().subscribe({
      next: (roles: Role[]) => {
        if (this.authService.hasRole('ROLE_SUPER_ADMIN')) {
          this.availableRoles = roles;
        } else if (this.authService.hasRole('ROLE_ADMIN')) {
          this.availableRoles = roles.filter(r => r.name !== 'ROLE_SUPER_ADMIN' && r.name !== 'ROLE_ADMIN');
        } else if (this.authService.hasRole('ROLE_COMPANY_SUPER_ADMIN')) {
          this.availableRoles = roles.filter(r => r.name !== 'ROLE_SUPER_ADMIN' && r.name !== 'ROLE_ADMIN');
        } else if (this.authService.hasRole('ROLE_COMPANY_ADMIN')) {
          this.availableRoles = roles.filter(r => r.name === 'ROLE_USER');
        }

        this.availableRoles.sort(
          (a, b) =>
            this.roleOrder.indexOf(a.name) - this.roleOrder.indexOf(b.name)
        );

        const userRole = roles.find(r => r.name === 'ROLE_USER');
        if (userRole && !this.rolesIds.includes(userRole.id)) {
          this.rolesIds.push(userRole.id);
        }
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar roles';
        this.errorVisible = true;
      }
    });
  }

  onRoleChange(event: Event, role: Role): void {
    const input = event.target as HTMLInputElement;
    if (input.checked) {
      if (!this.rolesIds.includes(role.id)) {
        this.rolesIds.push(role.id);
      }
    } else {
      this.rolesIds = this.rolesIds.filter(id => id !== role.id);
    }
  }

  onSubmit(): void {
    if (this.userForm.invalid) return;
    if (this.userForm.errors && this.userForm.errors['passwordsMismatch']) {
      this.errorMessage = 'La contraseña y la repetición no coinciden';
      this.errorVisible = true;
      return;
    }

    const userData: User = {
      id: this.isEditMode && this.userId ? this.userId : null,
      username: this.userForm.value.username,
      email: this.userForm.value.email,
      password: this.userForm.value.password,
      roles: this.availableRoles.filter(r => this.rolesIds.includes(r.id))
    };

    if (this.isEditMode && this.userId) {
      userData.id = this.userId;
      this.userService.update(userData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al actualizar el usuario';
          this.errorVisible = true;
        }
      });
    } else {
      this.userService.create(userData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al crear el usuario';
          this.errorVisible = true;
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/users']);
  }

  compareRoles(r1: Role, r2: Role): boolean {
    return r1 && r2 ? r1.id === r2.id : r1 === r2;
  }

  passwordsMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const repeat = group.get('repeat')?.value;

    if (password && repeat && password !== repeat) {
      return { passwordsMismatch: true };
    }
    return null;
  }

  get username() { return this.userForm.get('username'); }
  get email() { return this.userForm.get('email'); }
  get password() { return this.userForm.get('password'); }
  get repeat() { return this.userForm.get('password'); }
  get company() { return this.userForm.get('company'); }
  get employee() { return this.userForm.get('employee'); }
}
