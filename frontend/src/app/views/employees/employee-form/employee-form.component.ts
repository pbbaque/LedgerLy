import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Company } from '../../../models/company';
import { EmployeeService } from '../../../services/employee.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CompanyService } from '../../../services/company.service';
import { Employee } from '../../../models/employee';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { debounceTime, switchMap, catchError } from 'rxjs/operators';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrl: './employee-form.component.scss',
  standalone: false
})
export class EmployeeFormComponent {
  errorMessage = '';
  errorVisible = false;
  employeeForm!: FormGroup;
  isEditMode = false;
  employeeId: number | null = null;
  companies: Company[] = [];
  isAdmin = false;

  userOption: 'new' | 'existing' = 'existing';
  existingUserTerm = '';
  filteredUsers: any[] = [];
  selectedUser: any = null;

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private companyService: CompanyService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN') || this.authService.hasRole('ROLE_SUPER_ADMIN');

    this.employeeForm = this.fb.group({
      name: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      position: ['', Validators.required],
      salary: ['', Validators.required],
      company: this.fb.group({
        id: [null, this.isAdmin ? Validators.required : null],
        name: ['']
      }),
      user: this.fb.group({
        username: [''],
        password: [''],
        email: [''],
        roles: [[]]
      })
    });

    if (this.isAdmin) this.loadCompanies();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.employeeId = +id;
        this.loadEmployee(this.employeeId);
      }
    });
  }

  loadEmployee(id: number): void {
    this.employeeService.findById(id).subscribe({
      next: employee => {
        this.employeeForm.patchValue(employee);
        if (employee.user) {
          this.userOption = 'existing';
          this.selectedUser = employee.user;
        }
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar el empleado';
        this.errorVisible = true;
      }
    });
  }

  loadCompanies(): void {
    this.companyService.findAll().subscribe({
      next: companies => (this.companies = companies),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar las compañías';
        this.errorVisible = true;
      }
    });
  }

  private setUserValidators(enable: boolean): void {
    const userGroup = this.employeeForm.get('user') as FormGroup;
    if (!userGroup) return;

    const username = userGroup.get('username');
    const password = userGroup.get('password');
    const email = userGroup.get('email');

    if (enable) {
      username?.setValidators([Validators.required]);
      password?.setValidators([Validators.required]);
      email?.setValidators([Validators.required, Validators.email]);
    } else {
      username?.clearValidators();
      password?.clearValidators();
      email?.clearValidators();
    }

    username?.updateValueAndValidity();
    password?.updateValueAndValidity();
    email?.updateValueAndValidity();
  }

  searchUsers(): void {
    if (!this.existingUserTerm.trim()) {
      this.filteredUsers = [];
      return;
    }

    of(this.existingUserTerm).pipe(
      debounceTime(300),
      switchMap(term => this.userService.searchUsers(term).pipe(
        catchError(() => of([]))
      ))
    ).subscribe(users => this.filteredUsers = users);
  }

  selectUser(user: any): void {
    this.selectedUser = user;
    this.existingUserTerm = user.username;
    this.filteredUsers = [];
  }

  onSubmit(): void {
    if (this.employeeForm.invalid) return;

    const employeeData: Employee = this.employeeForm.value;
    employeeData.company.id = this.isAdmin ? this.employeeForm.value.company.id : this.authService.getCompanyId();

    if (this.userOption === 'existing') {
      employeeData.user = this.selectedUser;
    } else if (this.userOption === 'new') {
      this.setUserValidators(true);
    } else {
      delete employeeData.user;
    }

    let request$: Observable<Employee>;
    if (this.isEditMode) {
      request$ = this.employeeService.update({ ...employeeData, id: this.employeeId! });
    } else {
      request$ = this.userOption === 'new'
        ? this.employeeService.createWithUser(employeeData)
        : this.employeeService.create(employeeData);
    }

    request$.subscribe({
      next: () => this.goBack(),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || (this.isEditMode ? 'Error al actualizar el empleado' : 'Error al crear el empleado');
        this.errorVisible = true;
      }
    });
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }


  goBack(): void {
    this.router.navigate(['/employees']);
  }

  get name() { return this.employeeForm.get('name'); }
  get lastname() { return this.employeeForm.get('lastname'); }
  get email() { return this.employeeForm.get('email'); }
  get phone() { return this.employeeForm.get('phone'); }
  get position() { return this.employeeForm.get('position'); }
  get salary() { return this.employeeForm.get('salary'); }
  get company() { return this.employeeForm.get('company'); }
  get user() { return this.employeeForm.get('user'); }
}