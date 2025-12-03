import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CompanyService } from '../../../services/company.service';
import { Company } from '../../../models/company';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-company-form',
    templateUrl: './company-form.component.html',
    styleUrl: './company-form.component.scss',
    standalone: false
})
export class CompanyFormComponent {
errorMessage: string = '';
  errorVisible: boolean = false;
  companyForm!: FormGroup;
  isEditMode: boolean = false;
  companyId: number | null = null;
  companies: Company[] = [];
  isAdmin: boolean = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private companyService: CompanyService,
  ) { }

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

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.companyId = +id;
        this.loadCompany(this.companyId);
      }
    });
  }

  loadCompany(id: number): void {
    this.companyService.findById(id).subscribe({
      next: (company: Company) => {
        this.companyForm.patchValue(company);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar la empresa';
        this.errorVisible = true;
      }
    });
  }

  onSubmit() {
    if (this.companyForm.invalid)
      return;
    const companyData: Company = this.companyForm.value;
    if (this.isEditMode && this.companyId) {
      companyData.id = this.companyId;
      this.companyService.update(companyData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al actualizar la empresa';
          this.errorVisible = true;
        }
      });
    } else {
      this.companyService.create(companyData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al crear la empresa';
          this.errorVisible = true;
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/companies'])
  }

  get name() {
    return this.companyForm.get('name');
  }

  get fiscalNumber() {
    return this.companyForm.get('fiscalNumber');
  }

  get email() {
    return this.companyForm.get('email');
  }

  get phone() {
    return this.companyForm.get('phone');
  }

  get address() {
    return this.companyForm.get('address');
  }

  get company() {
    return this.companyForm.get('company');
  }
}
