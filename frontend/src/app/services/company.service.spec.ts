import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { CompanyService } from './company.service';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environments';
import { Company } from '../models/company';

describe('CompanyService', () => {
  let service: CompanyService;
  let httpMock: HttpTestingController;
  let authService: jasmine.SpyObj<AuthService>;

  const company: Company = {
    id: 1,
    name: 'Tech Solutions',
    fiscalNumber: 'B12345678',
    phone: '600000000',
    email: 'demo@ledgerly.local',
    address: {} as any
  };

  beforeEach(() => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['hasRole']);

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        CompanyService,
        { provide: AuthService, useValue: authService }
      ]
    });

    service = TestBed.inject(CompanyService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should load the current company for non-admin users', () => {
    authService.hasRole.and.returnValue(false);

    service.findAvailableForCurrentUser().subscribe(companies => {
      expect(companies).toEqual([company]);
    });

    const request = httpMock.expectOne(`${environment.apiUrl}/companies/current`);
    expect(request.request.method).toBe('GET');
    request.flush({
      success: true,
      data: company,
      message: 'OK',
      statusCode: 200
    });
  });

  it('should load all companies for admin users', () => {
    authService.hasRole.and.callFake(role => role === 'ROLE_ADMIN');

    service.findAvailableForCurrentUser().subscribe(companies => {
      expect(companies).toEqual([company]);
    });

    const request = httpMock.expectOne(`${environment.apiUrl}/companies`);
    expect(request.request.method).toBe('GET');
    request.flush({
      success: true,
      data: [company],
      message: 'OK',
      statusCode: 200
    });
  });
});
