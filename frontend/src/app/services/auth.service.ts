import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../models/api-response';
import { User } from '../models/user';
import { StorageService } from './storage.service';
import { JwtPayload } from '../models/jwtpayload';
import { jwtDecode } from 'jwt-decode';
import { Company } from '../models/company';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl: string = environment.apiUrl;
  private headers = { 'Content-Type': 'application/json' };

  private loggedIn: BehaviorSubject<boolean>;
  private userRoles: string[] = [];
  private userEmail: string = '';
  private userCompanyId: number | null = null;

  constructor(private http: HttpClient, private storageService: StorageService) {
    this.loggedIn = new BehaviorSubject<boolean>(this.hasToken());
    if (this.hasToken())
      this.decodeToken();
  }

  login(credentials: { email: string; password: string }): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}/auth/login`, credentials, { headers: this.headers }).pipe(
      tap(
        response => {
          const token = response.token;
          if (token) {
            this.storageService.saveToken(token);
            this.storageService.saveUser(response.data);
            this.decodeToken();
            this.loggedIn.next(true);
          }
        },
        error => {
          console.error('Login error', error);
        }
      )
    );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/users/current`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al obtener el usuario');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en getCurrentUser', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  getCurrentCompany(): Observable<Company> {
    return this.http.get<ApiResponse<Company>>(`${this.apiUrl}/companies/current`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al obtener la empresa');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en getCurrentCompany', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  logout(): void {
    this.storageService.clear();
    this.userRoles = [];
    this.userEmail = '';
    this.userCompanyId = null;
    this.loggedIn.next(false);
  }

  hasToken(): boolean {
    return !!this.storageService.getToken();
  }

  isLoggedIn(): boolean {
    return this.loggedIn.value;
  }

  getLoggedInObservable(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  private decodeToken(): void {
    const token = this.storageService.getToken();
    if (!token)
      return;

    try {
      const payload: JwtPayload = jwtDecode(token);
      const currentTime = Math.floor(Date.now() / 1000);
      if (payload.exp < currentTime) {
        this.logout();
        return;
      }

      this.userRoles = payload.authorities || [];
      this.userEmail = payload.sub;
      this.userCompanyId = payload.companyId || null;
    } catch (error) {
      console.error('Error decoding JWT:', error);
      this.logout();
    }
  }

  getRoles(): string[] {
    return this.userRoles;
  }

  getEmail(): string {
    return this.userEmail;
  }

  getCompanyId(): number | null {
    return this.userCompanyId;
  }

  hasRole(role: string): boolean {
    return this.userRoles.includes(role);
  }

  public checkTokenValidity(): void {
    this.decodeToken();
  }

  forgotPassword(email: string) {
    const params = new HttpParams().set('email', email);
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/auth/forgot-password`, null, { headers: this.headers, params: params });
  }


}
