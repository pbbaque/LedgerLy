import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { Invoice } from '../../../models/invoice';
import { Company } from '../../../models/company';
import { Client } from '../../../models/client';
import { Employee } from '../../../models/employee';

import { InvoiceService } from '../../../services/invoice.service';
import { CompanyService } from '../../../services/company.service';
import { ClientService } from '../../../services/client.service';
import { EmployeeService } from '../../../services/employee.service';

import { DetailConfig } from '../../../models/detail-config';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss'],
  standalone: false
})
export class InvoiceListComponent implements OnInit {
  invoices: Invoice[] = [];
  filteredInvoices: Invoice[] = [];

  companies: Company[] = [];
  clients: Client[] = [];
  employees: Employee[] = [];

  searchTerm: string = '';
  selectedCompanyId: number | '' = '';
  selectedClientId: number | '' = '';
  fromDate: string | null = null;
  toDate: string | null = null;

  noResults: boolean = false;
  errorMessage: string = '';
  errorVisible: boolean = false;

  showConfirmDelete: boolean = false;
  confirmMessage: string = '';
  invoiceToDelete: Invoice | null = null;

  selectedInvoice: Invoice | null = null;
  detailVisible: boolean = false;

  invoiceDetailConfig: DetailConfig = {
    title: 'Detalle de Factura',
    fields: [
      { key: 'id', label: 'ID' },
      { key: 'date', label: 'Fecha' },
      { key: 'client.name', label: 'Cliente' },
      { key: 'company.name', label: 'Empresa' },
      { key: 'employee.name', label: 'Empleado' },
      { key: 'description', label: 'Descripción' },
      { key: 'total', label: 'Total' }
    ]
  };

  totalAmount: number = 0;
  averageAmount: number = 0;
  pendingCount: number = 0;

  constructor(
    private invoiceService: InvoiceService,
    private companyService: CompanyService,
    private clientService: ClientService,
    private employeeService: EmployeeService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadLookups();
    this.loadInvoices();
  }

  private loadLookups(): void {
    const userRoles = this.authService.getRoles();

    if (userRoles.includes('SUPER_ADMIN') || userRoles.includes('ADMIN')) {
      this.companyService.findAll().subscribe({ next: c => this.companies = c, error: e => this.handleError(e) });
    }
    this.clientService.findAll().subscribe({ next: c => this.clients = c, error: e => this.handleError(e) });
    this.employeeService.findAll().subscribe({ next: e => this.employees = e, error: e => this.handleError(e) });
  }

  loadInvoices(): void {
    this.invoiceService.findAll().subscribe({
      next: (data) => {
        this.invoices = data;
        this.filteredInvoices = [...data];
        this.recalculateSummary();
        this.noResults = this.filteredInvoices.length === 0;
      },
      error: (error) => this.handleError(error)
    });
  }

  filterInvoices(): void {
    const term = (this.searchTerm || '').toLowerCase();
    this.filteredInvoices = this.invoices.filter(inv => {
      const matchesTerm =
        !term ||
        `${inv.id}`.includes(term) ||
        (inv.description || '').toLowerCase().includes(term) ||
        (inv.client?.name || '').toLowerCase().includes(term) ||
        (inv.company?.name || '').toLowerCase().includes(term);

      const matchesCompany = !this.selectedCompanyId || inv.company?.id === +this.selectedCompanyId;
      const matchesClient = !this.selectedClientId || inv.client?.id === +this.selectedClientId;

      let matchesFrom = true;
      let matchesTo = true;

      if (this.fromDate) {
        matchesFrom = new Date(inv.date) >= new Date(this.fromDate);
      }
      if (this.toDate) {
        matchesTo = new Date(inv.date) <= new Date(this.toDate);
      }

      return matchesTerm && matchesCompany && matchesClient && matchesFrom && matchesTo;
    });

    this.noResults = this.filteredInvoices.length === 0;
    this.recalculateSummary();
  }

  private recalculateSummary(): void {
    this.totalAmount = this.filteredInvoices.reduce((s, i) => s + (i.total ?? 0), 0);
    this.averageAmount = this.filteredInvoices.length ? this.totalAmount / this.filteredInvoices.length : 0;
    this.pendingCount = this.filteredInvoices.filter(i => (i as any).status === 'pending').length;
  }

  openDetail(inv: Invoice): void {
    this.selectedInvoice = { ...inv };
    this.detailVisible = true;
  }

  closeDetail(): void {
    this.selectedInvoice = null;
    this.detailVisible = false;
  }

  goToCreate(): void {
    this.router.navigate(['/invoices/create']);
  }

  goToEdit(inv: Invoice): void {
    this.router.navigate([`/invoices/edit/${inv.id}`]);
  }

  deleteInvoice(inv: Invoice): void {
    this.invoiceToDelete = inv;
    this.confirmMessage = `¿Seguro que desea eliminar la factura ${inv.id} (${inv.client?.name ?? 'sin cliente'})?`;
    this.showConfirmDelete = true;
  }

  openInvoicePage(invoice: Invoice) {
    this.router.navigate([`/invoices/${invoice.id}`]);
  }

  confirmDelete(): void {
    if (!this.invoiceToDelete) return;
    this.invoiceService.delete(this.invoiceToDelete.id).subscribe({
      next: () => this.loadInvoices(),
      error: (err) => this.handleError(err),
      complete: () => {
        this.invoiceToDelete = null;
        this.showConfirmDelete = false;
        this.confirmMessage = '';
      }
    });
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }


  cancelDelete(): void {
    this.invoiceToDelete = null;
    this.showConfirmDelete = false;
    this.confirmMessage = '';
  }

  private handleError(error: HttpErrorResponse | any): void {
    console.error('Error en Invoices:', error);
    this.errorMessage = error?.message || 'Error al procesar la petición. Intente de nuevo.';
    this.errorVisible = true;
  }
}
