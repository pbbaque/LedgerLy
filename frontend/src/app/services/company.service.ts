import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Company } from '../models/company';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  private apiUrl: string = environment.apiUrl + '/companies';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Company[]> {
    return this.http.get<ApiResponse<Company[]>>(`${this.apiUrl}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener las empresas');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findAll', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  registerCompany(company: Omit<Company, 'id'>): Observable<Company> {
    return this.http.post<Company>(`${this.apiUrl}`, company, { headers: this.headers });
  }

  findById(companyId: number): Observable<Company> {
    return this.http.get<ApiResponse<Company>>(`${this.apiUrl}/${companyId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener las empresas');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  searchCompanies(term: string): Observable<Company[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Company[]>>(`${this.apiUrl}/search/all`, { params }).pipe(
      map(response => response.data || []),
      catchError(error => {
        if (error.status === 404)
          return of<Company[]>([]);
        return throwError(() => error);
      })
    );
  }

  searchCompaniesByCompanyId(term: string, companyId: number): Observable<Company[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Company[]>>(`${this.apiUrl}/search/all/company/${companyId}`, { params }).pipe(
      map(response => response.data || [])
    );
  }

  create(companyData: Company): Observable<Company> {
    return this.http.post<ApiResponse<Company>>(`${this.apiUrl}`, companyData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al crear la empresa');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  update(companyData: Company): Observable<Company> {
    return this.http.put<ApiResponse<Company>>(`${this.apiUrl}`, companyData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al actualizar la empresa');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  delete(companyId: number): Observable<Company> {
    return this.http.delete<ApiResponse<Company>>(`${this.apiUrl}/${companyId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al eliminar la empresa');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en delete', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }
}
