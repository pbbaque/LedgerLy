import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Product } from '../../../models/product';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss'],
  standalone: false
})
export class ProductCardComponent {
  @Input() product!: Product;

  @Output() edit = new EventEmitter<Product>();
  @Output() delete = new EventEmitter<Product>();

  constructor(
    private authService: AuthService
  ) {}

  onEdit() {
    this.edit.emit(this.product);
  }

  onDelete() {
    this.delete.emit(this.product);
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }

}
