import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Invoice } from '../models/invoice';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { ApiResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  private apiUrl: string = environment.apiUrl + '/invoices';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Invoice[]> {
    return this.http.get<ApiResponse<Invoice[]>>(`${this.apiUrl}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener las facturas');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findAll', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  findById(invoiceId: number): Observable<Invoice> {
    return this.http.get<ApiResponse<Invoice>>(`${this.apiUrl}/${invoiceId}`, { headers: this.headers }).pipe(
      map(response => {

        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener la factura');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  getTotalInvoiced(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.apiUrl}/total`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting total invoiced');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getTotalInvoiced', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  getInvoicesThisMonth(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.apiUrl}/this-month`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting invoices this month');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getInvoicesThisMonth', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  getInvoicesToday(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${this.apiUrl}/today`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting invoices today');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getInvoicesToday', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  getMonthlyInvoices(): Observable<{ monthName: string, total: number }[]> {
    return this.http.get<ApiResponse<{ monthName: string, total: number }[]>>(`${this.apiUrl}/monthly`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting monthly invoices');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getMonthlyInvoices', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  getLatestInvoices(): Observable<Invoice[]> {
    return this.http.get<ApiResponse<Invoice[]>>(`${this.apiUrl}/latest`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting latest invoices');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getLatestInvoices', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  searchProducts(term: string): Observable<Invoice[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Invoice[]>>(`${this.apiUrl}/search/all`, { params }).pipe(
      map(response => response.data || []),
      catchError(error => {
        if (error.status === 404)
          return of<Invoice[]>([]);
        return throwError(() => error);
      })
    );
  }

  searchProductByCompanyId(term: string, companyId: number): Observable<Invoice[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Invoice[]>>(`${this.apiUrl}/search/all/company/${companyId}`, { params }).pipe(
      map(response => response.data || [])
    );
  }

  create(invoiceData: Invoice): Observable<Invoice> {
    return this.http.post<ApiResponse<Invoice>>(`${this.apiUrl}`, invoiceData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al crear la factura');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  update(invoiceData: Invoice): Observable<Invoice> {
    return this.http.put<ApiResponse<Invoice>>(`${this.apiUrl}`, invoiceData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al actualizar la factura');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  delete(invoiceId: number): Observable<Invoice> {
    return this.http.delete<ApiResponse<Invoice>>(`${this.apiUrl}/${invoiceId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al eliminar la factura');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en delete', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

}
