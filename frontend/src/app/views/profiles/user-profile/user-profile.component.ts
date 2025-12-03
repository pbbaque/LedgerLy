import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
  standalone: false
})
export class UserProfileComponent implements OnInit {
  errorMessage: string = '';
  errorVisible: boolean = false;
  profileForm!: FormGroup;
  userId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      repeat: ['']
    }, { validators: this.passwordsMatchValidator });

    // Cargar usuario actual desde el AuthService
    this.authService.getCurrentUser().subscribe({
      next: (currentUser: User) => {
        this.userId = currentUser.id || null;
        this.profileForm.patchValue({
          username: currentUser.username,
          email: currentUser.email
        });
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'Error al cargar el usuario';
        this.errorVisible = true;
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;
    if (this.profileForm.errors?.['passwordsMismatch']) {
      this.errorMessage = 'La contraseña y la repetición no coinciden';
      this.errorVisible = true;
      return;
    }

    const userData: User = {
      id: this.userId,
      username: this.profileForm.value.username,
      email: this.profileForm.value.email,
      password: this.profileForm.value.password || undefined,
      roles: []
    };

    this.userService.update(userData).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al actualizar el perfil';
        this.errorVisible = true;
      }
    });
  }

  passwordsMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const repeat = group.get('repeat')?.value;
    if (password && repeat && password !== repeat) {
      return { passwordsMismatch: true };
    }
    return null;
  }

  onReturn() {
    this.router.navigate(['/dashboard']);
  }

  get username() { return this.profileForm.get('username'); }
  get email() { return this.profileForm.get('email'); }
  get password() { return this.profileForm.get('password'); }
  get repeat() { return this.profileForm.get('repeat'); }
}
