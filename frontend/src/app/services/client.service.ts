import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApiResponse } from '../models/api-response';
import { Client } from '../models/client';
import { catchError, map, Observable, of, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private apiUrl: string = environment.apiUrl + '/clients';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Client[]> {
    return this.http.get<ApiResponse<Client[]>>(`${this.apiUrl}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los clientes');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findAll', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  findById(clientId: number): Observable<Client> {
    return this.http.get<ApiResponse<Client>>(`${this.apiUrl}/${clientId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los clientes');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  findByCompanyId(companyId: number): Observable<Client[]> {
    return this.http.get<ApiResponse<Client[]>>(`${this.apiUrl}/company/${companyId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los clientes');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  getTopClients(): Observable<{ name: string, lastname: string, totalInvoiced: number }[]> {
    return this.http.get<ApiResponse<{ name: string, lastname: string, totalInvoiced: number }[]>>(`${this.apiUrl}/top`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting top clients');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getTopClients', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  searchClients(term: string): Observable<Client[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Client[]>>(`${this.apiUrl}/search/all`, { params }).pipe(
      map(response => response.data || []),
      catchError(error => {
        if (error.status === 404)
          return of<Client[]>([]);
        return throwError(() => error);
      })
    );
  }

  searchClientsByCompanyId(term: string, companyId: number): Observable<Client[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Client[]>>(`${this.apiUrl}/search/all/company/${companyId}`, { params }).pipe(
      map(response => response.data || [])
    );
  }

  create(clientData: Client): Observable<Client> {
    return this.http.post<ApiResponse<Client>>(`${this.apiUrl}/${clientData.company.id}`, clientData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al obtener los clientes');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  update(clientData: Client): Observable<Client> {
    return this.http.put<ApiResponse<Client>>(`${this.apiUrl}/${clientData.company.id}`, clientData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al obtener los clientes');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  delete(clientId: number): Observable<Client> {
    return this.http.delete<ApiResponse<Client>>(`${this.apiUrl}/${clientId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al eliminar el cliente');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en delete', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }
}
