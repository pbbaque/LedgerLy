import { ComponentFixture, TestBed } from '@angular/core/testing';
import { componentTestImports, componentTestProviders, componentTestSchemas } from '../../../../test-support';

import { InvoiceDetailPageComponent } from './invoice-detail-page.component';

describe('InvoiceDetailPageComponent', () => {
  let component: InvoiceDetailPageComponent;
  let fixture: ComponentFixture<InvoiceDetailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: componentTestImports,
      providers: componentTestProviders,
      schemas: componentTestSchemas,
      declarations: [InvoiceDetailPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvoiceDetailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should calculate subtotal and tax for the loaded invoice', () => {
    component.invoice = {
      id: 3,
      description: 'Demo invoice',
      total: 399.30,
      date: '2026-06-18',
      employee: {} as any,
      client: {} as any,
      company: {} as any,
      details: [
        { invoiceId: 3, quantity: 1, unitPrice: 250, amount: 250, product: {} as any },
        { invoiceId: 3, quantity: 1, unitPrice: 80, amount: 80, product: {} as any }
      ]
    };

    expect(component.subtotal).toBe(330);
    expect(component.tax).toBeCloseTo(69.30, 2);
  });
});
