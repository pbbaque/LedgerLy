import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Invoice } from '../../../models/invoice';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-invoice-card',
  templateUrl: './invoice-card.component.html',
  styleUrls: ['./invoice-card.component.scss'],
  standalone: false
})
export class InvoiceCardComponent {
  @Input() invoice!: Invoice;

  @Output() openDetail = new EventEmitter<Invoice>();
  @Output() edit = new EventEmitter<Invoice>();
  @Output() delete = new EventEmitter<Invoice>();

  constructor(
    private authService: AuthService
  ) { }

  onOpenDetail() {
    this.openDetail.emit(this.invoice);
  }

  onEdit(event: MouseEvent) {
    event.stopPropagation();
    this.edit.emit(this.invoice);
  }

  onDelete(event: MouseEvent) {
    event.stopPropagation();
    this.delete.emit(this.invoice);
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }

}
