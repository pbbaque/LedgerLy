import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Invoice } from '../../../models/invoice';

@Component({
    selector: 'app-invoice-detail',
    templateUrl: './invoice-detail.component.html',
    styleUrls: ['./invoice-detail.component.scss'],
    standalone: false
})
export class InvoiceDetailComponent {
  @Input() invoice: Invoice | null = null;
  @Input() visible = false;

  @Output() closed = new EventEmitter<void>();
  @Output() edit = new EventEmitter<Invoice>();
  @Output() print = new EventEmitter<Invoice>();

  get subtotal(): number {
    return this.invoice?.details?.reduce(
      (sum, detail) => sum + ((detail.quantity || 0) * (detail.unitPrice || 0)),
      0
    ) || 0;
  }

  get tax(): number {
    return (this.invoice?.total || 0) - this.subtotal;
  }

  close() {
    this.closed.emit();
  }

  onEdit() {
    this.edit.emit(this.invoice!);
  }

  onPrint() {
    this.print.emit(this.invoice!);
  }
}
