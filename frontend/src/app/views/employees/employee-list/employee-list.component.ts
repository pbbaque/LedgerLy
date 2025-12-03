import { Component } from '@angular/core';
import { Employee } from '../../../models/employee';
import { DetailConfig } from '../../../models/detail-config';
import { EmployeeService } from '../../../services/employee.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AddressService } from '../../../services/address.service';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-employee-list',
    templateUrl: './employee-list.component.html',
    styleUrl: './employee-list.component.scss',
    standalone: false
})
export class EmployeeListComponent {

  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  paginatedEmployees: Employee[] = [];
  searchTerm: string = '';

  currentPage: number = 1;
  itemsPerPage: number = window.innerWidth >= 2000 ? 20 : 5;
  totalPages: number = 1;

  showConfirmDelete: boolean = false;
  confirmMessage: string = '';
  employeeToDeleteId: number | null = null;
  selectedEmployee: Employee | null = null;
  detailVisible: boolean = false;

  errorVisible: boolean = false;
  noResults: boolean = false;
  errorMessage: string = '';

  employeeDetailConfig: DetailConfig = {
    title: 'Detalle del Empleado',
    fields: [
      { key: 'id', label: 'ID' },
      { key: 'name', label: 'Nombre' },
      { key: 'lastname', label: 'Apellido' },
      { key: 'email', label: 'Correo' },
      { key: 'phone', label: 'Teléfono' },
      { key: 'position', label: 'Posición' },
      { key: 'salary', label: 'Salario' },
      { key: 'hireDate', label: 'Fecha de Contratación' },
      { key: 'user.username', label: 'Usuario' },
      { key: 'company.name', label: 'Compañía' },
    ]
  };

  constructor(
    private employeeService: EmployeeService,
    private addressService: AddressService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.employeeService.findAll().subscribe({
      next: (employees) => {
        this.employees = employees;
        this.filteredEmployees = [...employees];
        this.noResults = false;
        this.currentPage = 1;
        this.updatePagination();
      },
      error: (error) => this.handleError(error)
    });
  }

  searchEmployees(): void {
    const term = this.searchTerm.toLowerCase();

    if (!term) {
      this.filteredEmployees = [...this.employees];
      this.noResults = false;
      this.currentPage = 1;
      this.updatePagination();
      return;
    }

    if (this.employees.length <= 10) {
      this.filteredEmployees = this.employees.filter(employees =>
        employees.name.toLowerCase().includes(term) ||
        employees.lastname.toLowerCase().includes(term) ||
        employees.email.toLowerCase().includes(term) ||
        employees.phone.toLowerCase().includes(term) ||
        employees.position.toLowerCase().includes(term) ||
        employees.company.name.toLowerCase().includes(term) 
      );
      this.noResults = this.filteredEmployees.length === 0;
      this.currentPage = 1;
      this.updatePagination();
    } else {
      this.employeeService.searchEmployees(term).subscribe({
        next: (employees) => {
          this.filteredEmployees = employees
          this.noResults = employees.length === 0;
          this.currentPage = 1;
          this.updatePagination();
        },
        error: (error) => this.handleError(error)
      });
    }
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredEmployees.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedEmployees = this.filteredEmployees.slice(startIndex, endIndex);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.updatePagination();
  }

  onItemsPerPageChange(size: number): void {
    this.itemsPerPage = size;
    this.currentPage = 1;
    this.updatePagination();
  }


  goToCreate(): void {
    this.router.navigate(['/employees/create']);
  }

  goToEdit(clientId: number): void {
    this.router.navigate([`/employees/edit/${clientId}`]);
  }

  deleteEmployee(employee: Employee): void {
    this.employeeToDeleteId = employee.id;
    this.confirmMessage = '¿Está seguro de que desea eliminar al empleado ' + employee.id + ' ' + employee.name + ' ' + employee.lastname + '?';
    this.showConfirmDelete = true;
  }

  confirmDelete(): void {
    if (this.employeeToDeleteId !== null) {
      this.employeeService.delete(this.employeeToDeleteId).subscribe({
        next: () => this.loadEmployees(),
        error: (error) => this.handleError(error),
        complete: () => {
          this.employeeToDeleteId = null;
          this.showConfirmDelete = false;
          this.confirmMessage = '';
        }
      })
    }
  }

  cancelDelete(): void {
    this.employeeToDeleteId = null;
    this.showConfirmDelete = false;
    this.confirmMessage = '';
  }

  private handleError(error: HttpErrorResponse): void {
    console.error('Error al cargar los empleados:', error);
    this.errorMessage = error?.message || 'Error en la busqueda de empleados. Intentelos de nuevo.';
    this.errorVisible = true;
  }

  openDetail(employee: Employee): void {
    this.selectedEmployee = { ...employee };
    this.detailVisible = true;
  }

  
  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }

  closeDetail(): void {
    this.selectedEmployee = null;
    this.detailVisible = false;
  }

  formatPhone(country: string, phone: string): string {
    return this.addressService.formatPhone(country, phone);
  }
}
