import { Injectable } from '@angular/core';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private TOKEN_KEY = 'token';
  private USER_KEY = 'user';
  private REMEMBERED_EMAIL_KEY = 'rememberedEmail';

  constructor() {}

  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  saveUser(user: User): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  getUser(): User | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  saveRememberedEmail(email: string): void {
    localStorage.setItem(this.REMEMBERED_EMAIL_KEY, email);
  }

  getRememberedEmail(): string | null {
    return localStorage.getItem(this.REMEMBERED_EMAIL_KEY);
  }

  clear(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.REMEMBERED_EMAIL_KEY);
  }
}
