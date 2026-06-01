import { CommonModule } from '@angular/common';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

export const componentTestImports = [
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  RouterModule.forRoot([])
];

export const componentTestProviders = [
  provideHttpClient(),
  provideHttpClientTesting(),
  {
    provide: ActivatedRoute,
    useValue: {
      params: of({}),
      paramMap: of(convertToParamMap({})),
      queryParamMap: of(convertToParamMap({})),
      snapshot: {
        paramMap: convertToParamMap({}),
        queryParamMap: convertToParamMap({})
      }
    }
  }
];

export const componentTestSchemas = [NO_ERRORS_SCHEMA];
