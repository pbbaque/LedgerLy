import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { Invoice } from '../../../models/invoice';
import { InvoiceDetail } from '../../../models/invoice_detail';
import { Product } from '../../../models/product';
import { Company } from '../../../models/company';
import { Client } from '../../../models/client';
import { Employee } from '../../../models/employee';

import { InvoiceService } from '../../../services/invoice.service';
import { ProductService } from '../../../services/product.service';
import { CompanyService } from '../../../services/company.service';
import { ClientService } from '../../../services/client.service';
import { EmployeeService } from '../../../services/employee.service';

@Component({
    selector: 'app-invoice-form',
    templateUrl: './invoice-form.component.html',
    styleUrls: ['./invoice-form.component.scss'],
    standalone: false
})
export class InvoiceFormComponent implements OnInit {
  invoice: Invoice = {
    id: 0,
    description: '',
    total: 0,
    date: new Date().toISOString().substring(0, 10),
    employee: {} as Employee,
    client: {} as Client,
    company: {} as Company,
    details: []
  };

  products: Product[] = [];
  companies: Company[] = [];
  clients: Client[] = [];
  employees: Employee[] = [];

  isEdit: boolean = false;
  errorMessage: string = '';
  errorVisible: boolean = false;

  subtotal: number = 0;
  tax: number = 0;
  totalInvoice: number = 0;

  constructor(
    private invoiceService: InvoiceService,
    private productService: ProductService,
    private companyService: CompanyService,
    private clientService: ClientService,
    private employeeService: EmployeeService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadLookups();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.loadInvoice(+id);
    } else {
      this.addDetail(); // start with one detail line
      this.recalculateTotals();
    }
  }

  private loadLookups(): void {
    this.productService.findAll().subscribe({ next: p => this.products = p, error: e => this.handleError(e) });
    this.companyService.findAll().subscribe({ next: c => this.companies = c, error: e => this.handleError(e) });
    this.clientService.findAll().subscribe({ next: c => this.clients = c, error: e => this.handleError(e) });
    this.employeeService.findAll().subscribe({ next: e => this.employees = e, error: e => this.handleError(e) });
  }

  private loadInvoice(id: number): void {
    this.invoiceService.findById(id).subscribe({
      next: inv => {
        this.invoice = { ...inv };
        this.invoice.date = this.invoice.date.substring(0, 10);
        this.invoice.details = this.invoice.details || [];
        if (this.invoice.details.length === 0) this.addDetail();

        // Guardar la compañía seleccionada
        const selectedCompanyId = this.invoice.company?.id;

        // Mapear la compañía del listado general
        this.invoice.company = this.companies.find(c => c.id === selectedCompanyId) || {} as Company;

        if (selectedCompanyId) {
          // Cargar clientes, empleados y productos filtrados por la compañía
          this.clientService.findByCompanyId(selectedCompanyId).subscribe({
            next: clients => {
              this.clients = clients;
              this.invoice.client = clients.find(cl => cl.id === this.invoice.client.id) || {} as Client;
            },
            error: err => this.handleError(err)
          });

          this.employeeService.findByCompanyId(selectedCompanyId).subscribe({
            next: employees => {
              this.employees = employees;
              this.invoice.employee = employees.find(e => e.id === this.invoice.employee.id) || {} as Employee;
            },
            error: err => this.handleError(err)
          });

          this.productService.findByCompanyId(selectedCompanyId).subscribe({
            next: products => {
              this.products = products;
              this.invoice.details = this.invoice.details?.map(d => ({
                ...d,
                product: products.find(p => p.id === d.product.id) || {} as Product
              }));
              this.recalculateTotals();
            },
            error: err => this.handleError(err)
          });
        } else {
          this.clients = [];
          this.employees = [];
          this.products = [];
        }
      },
      error: err => this.handleError(err)
    });
  }

  addDetail(): void {
    const newDetail: InvoiceDetail = {
      invoiceId: this.invoice.id || 0,
      quantity: 1,
      amount: 0,
      unitPrice: 0,
      product: {} as Product
    };
    this.invoice.details = [...(this.invoice.details || []), newDetail];
    this.recalculateTotals();
  }

  removeDetail(index: number): void {
    this.invoice.details?.splice(index, 1);
    if ((this.invoice.details?.length ?? 0) === 0) this.addDetail();
    this.recalculateTotals();
  }

  onDetailChange(index: number): void {
    const detail = this.invoice.details?.[index];
    if (!detail) return;
    detail.amount = (detail.quantity || 0) * (detail.unitPrice || 0);
    this.recalculateTotals();
  }

  onProductChange(index: number): void {
    const detail = this.invoice.details?.[index];
    if (!detail || !detail.product) return;

    const selectedProduct = this.products.find(p => p.id === detail.product.id);
    if (selectedProduct) {
      detail.unitPrice = selectedProduct.price;
      detail.quantity = detail.quantity || 1;
      detail.amount = detail.unitPrice * detail.quantity;
    }

    this.recalculateTotals();
  }

  onCompanyChange(): void {
    if (!this.invoice.company || !this.invoice.company.id) {
      this.clients = [];
      this.employees = [];
      this.products = [];
      this.invoice.client = {} as Client;
      this.invoice.employee = {} as Employee;
      return;
    }

    this.invoice.client = {} as Client;
    this.invoice.employee = {} as Employee;

    this.clientService.findByCompanyId(this.invoice.company.id).subscribe({
      next: (clients) => (this.clients = clients),
      error: (err) => this.handleError(err)
    });

    this.employeeService.findByCompanyId(this.invoice.company.id).subscribe({
      next: (employees) => (this.employees = employees),
      error: (err) => this.handleError(err)
    });

    this.productService.findByCompanyId(this.invoice.company.id).subscribe({
      next: (products) => (this.products = products),
      error: (err) => this.handleError(err)
    });
  }

  private recalculateTotals(): void {
    this.subtotal = (this.invoice.details || []).reduce((s, d) => s + ((d.quantity || 0) * (d.unitPrice || 0)), 0);
    this.tax = this.subtotal * 0.21;
    this.totalInvoice = this.subtotal + this.tax;
    this.invoice.total = this.totalInvoice;
  }

  save(): void {
    this.recalculateTotals();
    if (this.isEdit) {
      this.invoiceService.update(this.invoice).subscribe({
        next: () => this.router.navigate(['/invoices']),
        error: err => this.handleError(err)
      });
    } else {
      this.invoiceService.create(this.invoice).subscribe({
        next: () => this.router.navigate(['/invoices']),
        error: err => this.handleError(err)
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/invoices']);
  }

  private handleError(error: HttpErrorResponse | any): void {
    console.error('InvoiceForm error:', error);
    this.errorMessage = error?.message || 'Error procesando la petición.';
    this.errorVisible = true;
  }
}
