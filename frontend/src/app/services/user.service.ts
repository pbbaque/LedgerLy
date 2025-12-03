import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { User } from '../models/user';
import { ApiResponse } from '../models/api-response';
import { Role } from '../models/role';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl: string = environment.apiUrl + '/users';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient, private authService:AuthService) {}

  findAll(): Observable<User[]> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al obtener los usuarios');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en findAll', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  findAllByCompany(companyId: number | null): Observable<User[]> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/company/${companyId}`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al obtener los usuarios');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en findAll', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  findById(userId: number): Observable<User> {
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/${userId}`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al obtener el usuario');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en findById', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  searchUsers(term: string): Observable<User[]> {
    const params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/search/all`, { params }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al buscar usuarios');
        return res.data || [];
      }),
      catchError(err => {
        console.error('Error en searchUsers', err);
        if (err.status === 404) return of([]);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  searchUsersByCompany(term: string, companyId: number): Observable<User[]> {
    const params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/search/company/${companyId}`, { params }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al buscar usuarios por empresa');
        return res.data || [];
      }),
      catchError(err => {
        console.error('Error en searchUsersByCompany', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  create(user: User): Observable<User> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}`, user, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success || !res.data) throw new Error(res.message || 'Error desconocido al crear el usuario');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en create', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  createForCompany(user: User, companyId: number): Observable<User> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}/for-company/${companyId}`, user, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success || !res.data) throw new Error(res.message || 'Error desconocido al crear usuario para empresa');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en createForCompany', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  register(user: User): Observable<User> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}/register`, user, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success || !res.data) throw new Error(res.message || 'Error desconocido al registrar el usuario');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en register', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  update(user: User): Observable<User> {
    const url = user.employee?.company?.id ? `${this.apiUrl}` : `${this.apiUrl}`;
    return this.http.put<ApiResponse<User>>(url, user, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success || !res.data) throw new Error(res.message || 'Error desconocido al actualizar el usuario');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en update', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  delete(userId: number): Observable<User> {
    return this.http.delete<ApiResponse<User>>(`${this.apiUrl}/${userId}`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success || !res.data) throw new Error(res.message || 'Error desconocido al eliminar el usuario');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en delete', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  forgotPassword(email: string): Observable<void> {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/forgot-password?email=${email}`, null, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al procesar la contraseña');
        return;
      }),
      catchError(err => {
        console.error('Error en forgotPassword', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }

  findAllRoles(): Observable<Role[]> {
    return this.http.get<ApiResponse<Role[]>>(`${this.apiUrl}/roles`, { headers: this.headers }).pipe(
      map(res => {
        if (!res.success) throw new Error(res.message || 'Error desconocido al obtener los usuarios');
        return res.data;
      }),
      catchError(err => {
        console.error('Error en findAll', err);
        return throwError(() => new Error(err.message || 'Error en la solicitud'));
      })
    );
  }


}
