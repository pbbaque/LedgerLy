import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Company } from '../../../models/company';
import { CompanyService } from '../../../services/company.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrl: './register.component.scss',
    standalone: false
})
export class RegisterComponent {

  registerForm!: FormGroup;

  constructor( 
    private fb: FormBuilder, 
    private companyService: CompanyService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      fiscalNumber: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      email: ['', [Validators.required, Validators.email]],
      address: this.fb.group({
        street: ['', Validators.required],
        city: ['', Validators.required],
        country: ['', Validators.required],
        number: ['', [Validators.required, Validators.pattern(/^\d{5}$/)]]
      })
    });
  }

  onSubmit(): void {
    if ( this.registerForm.invalid ) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const company = this.buildCompanyFromForm();

    this.companyService.registerCompany(company).subscribe({
      next: (response) => {
        alert('Empresa registrada con éxito');
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        console.error('Error al registrar la empresa:', error);
        alert('Error al registrar la empresa: ' + (error?.error?.message || error.message || 'Error desconocido'));
      }
    });
  }

  private buildCompanyFromForm(): Omit<Company, 'id'> {
    const formValue = this.registerForm.value;

    return {
      name: formValue.name,
      fiscalNumber: formValue.fiscalNumber,
      phone: formValue.phone,
      email: formValue.email,
      address: {
        street: formValue.address.street,
        city: formValue.address.city,
        country: formValue.address.country,
        number: formValue.address.number
      }
    }
  }


  get name() { return this.registerForm.get('name'); }
  get fiscalNumber() { return this.registerForm.get('fiscalNumber'); }
  get phone() { return this.registerForm.get('phone'); }
  get email() { return this.registerForm.get('email'); }
  get address() { return this.registerForm.get('address'); }

}
