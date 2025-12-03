import { Component } from '@angular/core';
import { Company } from '../../../models/company';
import { DetailConfig } from '../../../models/detail-config';
import { CompanyService } from '../../../services/company.service';
import { AddressService } from '../../../services/address.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Address } from '../../../models/address';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  styleUrl: './company-list.component.scss',
  standalone: false
})
export class CompanyListComponent {

  companies: Company[] = [];
  filteredCompanies: Company[] = [];
  paginatedCompanies: Company[] = [];
  searchTerm: string = '';

  currentPage: number = 1;
  itemsPerPage: number = window.innerWidth >= 2000 ? 20 : 5;
  totalPages: number = 1;

  showConfirmDelete: boolean = false;
  confirmMessage: string = '';
  companyToDeleteId: number | null = null;
  selectedCompany: Company | null = null;
  detailVisible: boolean = false;

  errorVisible: boolean = false;
  noResults: boolean = false;
  errorMessage: string = '';

  companyDetailConfig: DetailConfig = {
    title: 'Detalle de la Empresa',
    fields: [
      { key: 'id', label: 'ID' },
      { key: 'name', label: 'Nombre' },
      { key: 'fiscalNumber', label: 'NIF/CIF' },
      { key: 'email', label: 'Correo' },
      { key: 'phone', label: 'Teléfono' },
      { key: 'address', label: 'Dirección' }
    ]
  };

  constructor(
    private companyService: CompanyService,
    private addressService: AddressService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.companyService.findAll().subscribe({
      next: (companies) => {
        this.companies = companies;
        this.filteredCompanies = [...companies];
        this.noResults = false;
        this.currentPage = 1;
        this.updatePagination();
      },
      error: (error) => this.handleError(error)
    });
  }

  searchCompanies(): void {
    const term = this.searchTerm.toLowerCase();

    if (!term) {
      this.filteredCompanies = [...this.companies];
      this.noResults = false;
      this.currentPage = 1;
      this.updatePagination();
      return;
    }

    if (this.companies.length <= 10) {
      this.filteredCompanies = this.companies.filter(companies =>
        companies.name.toLowerCase().includes(term) ||
        companies.fiscalNumber.toLowerCase().includes(term) ||
        companies.email.toLowerCase().includes(term) ||
        companies.phone.toLowerCase().includes(term) ||
        this.formatAddress(companies.address).toLowerCase().includes(term)
      );
      this.noResults = this.filteredCompanies.length === 0;
      this.currentPage = 1;
      this.updatePagination();
    } else {
      this.companyService.searchCompanies(term).subscribe({
        next: (companies) => {
          this.filteredCompanies = companies
          this.noResults = companies.length === 0;
          this.currentPage = 1;
          this.updatePagination();
        },
        error: (error) => this.handleError(error)
      });
    }
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredCompanies.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedCompanies = this.filteredCompanies.slice(startIndex, endIndex);
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
    this.router.navigate(['/companies/create']);
  }

  goToEdit(companyId: number): void {
    this.router.navigate([`/companies/edit/${companyId}`]);
  }

  deleteCompany(company: Company): void {
    this.companyToDeleteId = company.id;
    this.confirmMessage = '¿Está seguro de que desea eliminar la empresa ' + company.id + ' ' + company.name + ' ?';
    this.showConfirmDelete = true;
  }

  confirmDelete(): void {
    if (this.companyToDeleteId !== null) {
      this.companyService.delete(this.companyToDeleteId).subscribe({
        next: () => this.loadCompanies(),
        error: (error) => this.handleError(error),
        complete: () => {
          this.companyToDeleteId = null;
          this.showConfirmDelete = false;
          this.confirmMessage = '';
        }
      })
    }
  }

  cancelDelete(): void {
    this.companyToDeleteId = null;
    this.showConfirmDelete = false;
    this.confirmMessage = '';
  }

  private handleError(error: HttpErrorResponse): void {
    console.error('Error al cargar las empresa:', error);
    this.errorMessage = error?.message || 'Error en la busqueda de empresas. Intentelos de nuevo.';
    this.errorVisible = true;
  }

  openDetail(company: Company): void {
    this.selectedCompany = { ...company };
    this.detailVisible = true;
  }

  closeDetail(): void {
    this.selectedCompany = null;
    this.detailVisible = false;
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }


  formatAddress(address: Address): string {
    return this.addressService.formatAddress(address);
  }

  formatPhone(country: string, phone: string): string {
    return this.addressService.formatPhone(country, phone);
  }
}
