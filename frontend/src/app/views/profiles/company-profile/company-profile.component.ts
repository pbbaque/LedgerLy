import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { CompanyService } from '../../../services/company.service';
import { Company } from '../../../models/company';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-company-profile',
  templateUrl: './company-profile.component.html',
  styleUrls: ['./company-profile.component.scss'],
  standalone: false
})
export class CompanyProfileComponent implements OnInit {
  errorMessage: string = '';
  errorVisible: boolean = false;
  companyForm!: FormGroup;
  companyId: number | null = null;
  isEditMode: boolean = true;

  constructor(
    private fb: FormBuilder,
    private companyService: CompanyService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.companyForm = this.fb.group({
      name: ['', [Validators.required]],
      fiscalNumber: ['', [Validators.required]],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        country: ['', Validators.required],
        number: ['', [Validators.required, Validators.pattern(/^\d{5}$/)]]
      })
    });

    this.loadCompany();
  }

  loadCompany(): void {
    this.authService.getCurrentCompany().subscribe({
      next: (company: Company) => {
        this.companyId = company.id;
        this.companyForm.patchValue(company);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar la empresa';
        this.errorVisible = true;
      }
    });
  }

  onSubmit(): void {
    if (this.companyForm.invalid) return;

    const companyData: Company = this.companyForm.value;
    if (this.companyId) companyData.id = this.companyId;

    this.companyService.update(companyData).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al actualizar la empresa';
        this.errorVisible = true;
      }
    });
  }

  get name() { return this.companyForm.get('name'); }
  get fiscalNumber() { return this.companyForm.get('fiscalNumber'); }
  get email() { return this.companyForm.get('email'); }
  get phone() { return this.companyForm.get('phone'); }
  get address() { return this.companyForm.get('address'); }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}
