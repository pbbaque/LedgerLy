import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Product } from '../models/product';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { ApiResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl: string = environment.apiUrl + '/products';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Product[]> {
    return this.http.get<ApiResponse<Product[]>>(`${this.apiUrl}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los productos');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findAll', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  findById(productId: number): Observable<Product> {
    return this.http.get<ApiResponse<Product>>(`${this.apiUrl}/${productId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los productos');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  findByCompanyId(companyId: number): Observable<Product[]> {
    return this.http.get<ApiResponse<Product[]>>(`${this.apiUrl}/company/${companyId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los productos');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  getTopProducts(): Observable<{ name: string, quantitySold: number }[]> {
    return this.http.get<ApiResponse<{ name: string, quantitySold: number }[]>>(`${this.apiUrl}/top`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success) throw new Error(response.message || 'Unknown error getting top products');
        return response.data;
      }),
      catchError(error => {
        console.error('Error in getTopProducts', error);
        return throwError(() => new Error(error.message || 'Request error'));
      })
    );
  }

  searchProducts(term: string): Observable<Product[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Product[]>>(`${this.apiUrl}/search/all`, { params }).pipe(
      map(response => response.data || []),
      catchError(error => {
        if (error.status === 404)
          return of<Product[]>([]);
        return throwError(() => error);
      })
    );
  }

  searchProductByCompanyId(term: string, companyId: number): Observable<Product[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Product[]>>(`${this.apiUrl}/search/all/company/${companyId}`, { params }).pipe(
      map(response => response.data || [])
    );
  }

  create(productData: Product): Observable<Product> {
    return this.http.post<ApiResponse<Product>>(`${this.apiUrl}`, productData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al crear el producto');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  update(productData: Product): Observable<Product> {
    return this.http.put<ApiResponse<Product>>(`${this.apiUrl}`, productData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al actualizar el producto');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  delete(productId: number): Observable<Product> {
    return this.http.delete<ApiResponse<Product>>(`${this.apiUrl}/${productId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al eliminar el producto');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en delete', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }
}
