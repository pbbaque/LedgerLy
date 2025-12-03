import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-forget',
    templateUrl: './forget.component.html',
    styleUrl: './forget.component.scss',
    standalone: false
})
export class ForgetComponent implements OnInit {
  
  forgotForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.forgotForm.invalid) {
      this.forgotForm.markAllAsTouched();
      return;
    }

    const { email } = this.forgotForm.value;
    this.authService.forgotPassword(email).subscribe({
      next: (response) => {
        alert(response.message || 'Si el correo existe se enviaron instrucciones.');
        this.router.navigate(['/auth/login']);
      },
      error: (error) => {
        console.error('Error al recuperar contraseña:', error);
        const message = error?.error?.message || 'Error desconocido';
        alert(message);
      }
    });
  }

  get email() {
    return this.forgotForm.get('email');
  }
}
