import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Invoice } from '../../../models/invoice';
import { InvoiceService } from '../../../services/invoice.service';

@Component({
    selector: 'app-invoice-detail-page',
    templateUrl: './invoice-detail-page.component.html',
    styleUrls: ['./invoice-detail-page.component.scss'],
    standalone: false
})
export class InvoiceDetailPageComponent implements OnInit {
  invoice: Invoice | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadInvoice(+id);
    }
  }

  loadInvoice(id: number) {
    this.invoiceService.findById(id).subscribe({
      next: (invoice) => {
        this.invoice = invoice;
      },
      error: () => this.router.navigate(['/invoices'])
    });
  }

  get subtotal(): number {
    return this.invoice?.details?.reduce(
      (sum, detail) => sum + ((detail.quantity || 0) * (detail.unitPrice || 0)),
      0
    ) || 0;
  }

  get tax(): number {
    return (this.invoice?.total || 0) - this.subtotal;
  }

  print() {
    window.print();
  }

  goBack() {
    this.router.navigate(['/invoices']);
  }
}
