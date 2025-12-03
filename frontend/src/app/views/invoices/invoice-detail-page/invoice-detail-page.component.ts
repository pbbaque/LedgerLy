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

  print() {
    window.print();
  }

  goBack() {
    this.router.navigate(['/invoices']);
  }
}
