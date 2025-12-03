import { Component, OnInit } from '@angular/core';
import { User } from '../../../models/user';
import { DetailConfig } from '../../../models/detail-config';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
  standalone: false
})
export class UserListComponent implements OnInit {

  users: User[] = [];
  filteredUsers: User[] = [];
  paginatedUsers: User[] = [];
  searchTerm: string = '';

  currentPage: number = 1;
  itemsPerPage: number = window.innerWidth >= 2000 ? 20 : 5;
  totalPages: number = 1;

  showConfirmDelete: boolean = false;
  confirmMessage: string = '';
  userToDeleteId: number | null = null;
  selectedUser: User | null = null;
  detailVisible: boolean = false;

  errorMessage: string = '';
  errorVisible: boolean = false;
  noResults: boolean = false;

  userDetailConfig: DetailConfig = {
    title: 'Detalle del Usuario',
    fields: [
      { key: 'id', label: 'ID' },
      { key: 'username', label: 'Usuario' },
      { key: 'email', label: 'Correo' },
      { key: 'employee.name', label: 'Nombre' },
      { key: 'employee.lastname', label: 'Apellido' },
      { key: 'employee.company.name', label: 'Empresa' },
    ]
  };

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.findAll().subscribe({
      next: (users) => {
        this.users = users;
        this.filteredUsers = [...users];
        this.noResults = false;
        this.currentPage = 1;
        this.updatePagination();
      },
      error: (error) => this.handleError(error)
    });
  }

  searchUsers(): void {
    const term = this.searchTerm.toLowerCase();

    if (!term) {
      this.filteredUsers = [...this.users];
      this.noResults = false;
      this.currentPage = 1;
      this.updatePagination();
      return;
    }

    if (this.users.length <= 10) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(term) ||
        user.email.toLowerCase().includes(term) ||
        user.employee?.name.toLowerCase().includes(term) ||
        user.employee?.lastname.toLowerCase().includes(term) ||
        user.employee?.company?.name.toLowerCase().includes(term)
      );
      this.noResults = this.filteredUsers.length === 0;
      this.currentPage = 1;
      this.updatePagination();
    } else {
      this.userService.searchUsers(term).subscribe({
        next: (users) => {
          this.filteredUsers = users;
          this.noResults = users.length === 0;
          this.currentPage = 1;
          this.updatePagination();
        },
        error: (error) => this.handleError(error)
      });
    }
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredUsers.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedUsers = this.filteredUsers.slice(startIndex, endIndex);
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
    this.router.navigate(['/users/create']);
  }

  goToEdit(userId: number | null): void {
    this.router.navigate([`/users/edit/${userId}`]);
  }

  deleteUser(user: User): void {
    this.userToDeleteId = user.id;
    this.confirmMessage = '¿Está seguro de que desea eliminar al usuario ' + user.id + ' ' + user.username + '?';
    this.showConfirmDelete = true;
  }

  confirmDelete(): void {
    if (this.userToDeleteId !== null) {
      this.userService.delete(this.userToDeleteId).subscribe({
        next: () => this.loadUsers(),
        error: (error) => this.handleError(error),
        complete: () => {
          this.userToDeleteId = null;
          this.showConfirmDelete = false;
          this.confirmMessage = '';
        }
      })
    }
  }

  cancelDelete(): void {
    this.userToDeleteId = null;
    this.showConfirmDelete = false;
    this.confirmMessage = '';
  }

  private handleError(error: HttpErrorResponse): void {
    console.error('Error al cargar los usuarios:', error);
    this.errorMessage = error?.message || 'Error en la busqueda de usuarios. Intentelo de nuevo.';
    this.errorVisible = true;
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }


  openDetail(user: User): void {
    this.selectedUser = { ...user };
    this.detailVisible = true;
  }

  closeDetail(): void {
    this.selectedUser = null;
    this.detailVisible = false;
  }
}