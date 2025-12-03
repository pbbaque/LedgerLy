import { Injectable } from '@angular/core';
import { environment } from '../../environments/environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map, Observable, of, throwError } from 'rxjs';
import { Employee } from '../models/employee';
import { ApiResponse } from '../models/api-response';
import { EmployeeWithUserDTO } from '../models/dtos/employee-with-user-dto';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private apiUrl: string = environment.apiUrl + '/employees';
  private headers = { 'Content-Type': 'application/json' };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Employee[]> {
    return this.http.get<ApiResponse<Employee[]>>(`${this.apiUrl}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los empleados');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findAll', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  findById(employeeId: number): Observable<Employee> {
    return this.http.get<ApiResponse<Employee>>(`${this.apiUrl}/${employeeId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los empleados');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  findByCompanyId(companyId: number): Observable<Employee[]> {
    return this.http.get<ApiResponse<Employee[]>>(`${this.apiUrl}/company/${companyId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success)
          throw new Error(response.message || 'Error desconocido al obtener los empleados');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en findById', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    )
  }

  searchEmployees(term: string): Observable<Employee[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Employee[]>>(`${this.apiUrl}/search/all`, { params }).pipe(
      map(response => response.data || []),
      catchError(error => {
        if (error.status === 404)
          return of<Employee[]>([]);
        return throwError(() => error);
      })
    );
  }

  searchEmployeesByCompanyId(term: string, companyId: number): Observable<Employee[]> {
    let params = new HttpParams().set('term', term);
    return this.http.get<ApiResponse<Employee[]>>(`${this.apiUrl}/search/all/company/${companyId}`, { params }).pipe(
      map(response => response.data || [])
    );
  }

  create(employeeData: Employee): Observable<Employee> {
    return this.http.post<ApiResponse<Employee>>(`${this.apiUrl}`, employeeData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al obtener los empleados');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

createWithUser(employeeData: Employee): Observable<Employee> {
  const dto: EmployeeWithUserDTO = {
    employee: { ...employeeData, user: undefined }, 
    user: employeeData.user || {}                  
  };

  return this.http.post<ApiResponse<Employee>>(
    `${this.apiUrl}/with-user`,
    dto,
    { headers: this.headers }
  ).pipe(
    map(response => {
      if (!response.success || !response.data) {
        throw new Error(response.message || 'Error desconocido al crear el empleado con usuario');
      }
      return response.data;
    }),
    catchError(error => {
      console.error('Error en createWithUser', error);
      return throwError(() => new Error(error.message || 'Error en la solicitud'));
    })
  );
}

  update(employeeData: Employee): Observable<Employee> {
    return this.http.put<ApiResponse<Employee>>(`${this.apiUrl}/${employeeData.company.id}`, employeeData, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al obtener los employee');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en create', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }

  delete(employeeId: number): Observable<Employee> {
    return this.http.delete<ApiResponse<Employee>>(`${this.apiUrl}/${employeeId}`, { headers: this.headers }).pipe(
      map(response => {
        if (!response.success && !response.data)
          throw new Error(response.message || 'Error desconocido al eliminar el empleado');
        return response.data;
      }),
      catchError(error => {
        console.error('Error en delete', error);
        return throwError(() => new Error(error.message || 'Error en la solicitud'));
      })
    );
  }
}
