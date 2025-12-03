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
