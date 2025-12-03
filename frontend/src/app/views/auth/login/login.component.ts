import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { StorageService } from '../../../services/storage.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrl: './login.component.scss',
    standalone: false
})
export class LoginComponent implements OnInit {
  
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService, 
    private storageService: StorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const rememberedEmail = this.storageService.getRememberedEmail();

    this.loginForm = this.fb.group({
      email: [rememberedEmail || '', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      remember: [!!rememberedEmail] 
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password, remember } = this.loginForm.value;
    this.authService.login({ email, password }).subscribe({
      next: (response) => {
        if(response.success) {
          if (remember) {
            this.storageService.saveRememberedEmail(email);
          } else {
            this.storageService.saveRememberedEmail('');
          }

          this.router.navigate(['/dashboard']);
        } else {
          console.error('Login failed:', response.message);
          alert('Error al iniciar sesión: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Login error:', error);
        const message = error?.error?.message || error.message || 'Error desconocido';
        alert('Error al iniciar sesión: ' + message);
      }
    });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
