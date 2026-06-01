import { ComponentFixture, TestBed } from '@angular/core/testing';
import { componentTestImports, componentTestProviders, componentTestSchemas } from '../../../../test-support';

import { InvoiceCardComponent } from './invoice-card.component';

describe('InvoiceCardComponent', () => {
  let component: InvoiceCardComponent;
  let fixture: ComponentFixture<InvoiceCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: componentTestImports,
      providers: componentTestProviders,
      schemas: componentTestSchemas,
      declarations: [InvoiceCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvoiceCardComponent);
    component = fixture.componentInstance;
    component.invoice = {
      id: 1,
      date: '2026-06-01',
      description: 'Demo invoice',
      total: 100,
      client: { id: 1, name: 'Demo client' } as any,
      company: { id: 1, name: 'Demo company' } as any,
      employee: { id: 1, name: 'Demo employee' } as any,
      invoiceDetails: []
    } as any;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
