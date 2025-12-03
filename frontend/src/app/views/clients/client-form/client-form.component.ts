import { Component, OnInit } from '@angular/core';
import { Client } from '../../../models/client';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from '../../../services/client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Company } from '../../../models/company';
import { AuthService } from '../../../services/auth.service';
import { CompanyService } from '../../../services/company.service';
import { AddressService } from '../../../services/address.service';

@Component({
    selector: 'app-client-form',
    templateUrl: './client-form.component.html',
    styleUrl: './client-form.component.scss',
    standalone: false
})
export class ClientFormComponent implements OnInit {
  errorMessage: string = '';
  errorVisible: boolean = false;
  clientForm!: FormGroup;
  isEditMode: boolean = false;
  clientId: number | null = null;
  companies: Company[] = [];
  isAdmin: boolean = false;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private companyService: CompanyService,
    private addressService: AddressService
  ) { }

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN') || this.authService.hasRole('ROLE_SUPER_ADMIN');

    this.clientForm = this.fb.group({
      name: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      company: this.fb.group({
        id: [null, this.isAdmin ? Validators.required : null],
        name: ['']
      }),
      address: this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        country: ['', Validators.required],
        number: ['', [Validators.required, Validators.pattern(/^\d{5}$/)]]
      })
    });

    if (this.isAdmin)
      this.loadCompanies();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.clientId = +id;
        this.loadClient(this.clientId);
      }
    });
  }

  loadClient(id: number): void {
    this.clientService.findById(id).subscribe({
      next: (client: Client) => {
        this.clientForm.patchValue(client);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar el cliente';
        this.errorVisible = true;
      }
    });
  }

  loadCompanies(): void {
    this.companyService.findAll().subscribe({
      next: (companies: Company[]) => this.companies = companies,
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar las compañías';
        this.errorVisible = true;
      }
    });
  }

  onSubmit() {
    if (this.clientForm.invalid)
      return;
    const clientData: Client = this.clientForm.value;
    clientData.company.id = this.isAdmin ? this.clientForm.value.company.id : this.authService.getCompanyId();
    if (this.isEditMode && this.clientId) {
      clientData.id = this.clientId;
      this.clientService.update(clientData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al actualizar el cliente';
          this.errorVisible = true;
        }
      });
    } else {
      this.clientService.create(clientData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al crear el cliente';
          this.errorVisible = true;
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/clients'])
  }

  get name() {
    return this.clientForm.get('name');
  }

  get lastname() {
    return this.clientForm.get('lastname');
  }

  get email() {
    return this.clientForm.get('email');
  }

  get phone() {
    return this.clientForm.get('phone');
  }

  get address() {
    return this.clientForm.get('address');
  }

  get company() {
    return this.clientForm.get('company');
  }
}
