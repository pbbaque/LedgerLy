import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ProductService } from '../../../services/product.service';
import { Product } from '../../../models/product';

import { register } from 'swiper/element/bundle';
import { DetailConfig } from '../../../models/detail-config';
import { AuthService } from '../../../services/auth.service';


register();

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  standalone: false
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  filteredProducts: Product[] = [];
  groupedProducts: { [companyName: string]: Product[] } = {};
  groupedCompanyNames: string[] = [];
  searchTerm: string = '';

  isMobile: boolean = window.innerWidth < 1200;

  noResults: boolean = false;
  errorMessage: string = '';
  errorVisible: boolean = false;

  showConfirmDelete: boolean = false;
  confirmMessage: string = '';
  productToDeleteId: number | null = null;
  selectedProduct: Product | null = null;
  detailVisible: boolean = false;

  productDetailConfig: DetailConfig = {
    title: 'Detalle del Producto',
    fields: [
      { key: 'id', label: 'ID' },
      { key: 'name', label: 'Nombre' },
      { key: 'description', label: 'Descripción' },
      { key: 'price', label: 'Precio' },
      { key: 'sku', label: 'SKU' },
      { key: 'stock', label: 'Stock' },
      { key: 'company.name', label: 'Empresa' },
    ]
  };

  constructor(
    private productService: ProductService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.isMobile = window.innerWidth < 1200;
  }


  loadProducts(): void {
    this.productService.findAll().subscribe({
      next: (products) => {
        this.products = products;
        this.filteredProducts = [...products];
        this.noResults = this.filteredProducts.length === 0;
        this.groupProducts();
      },
      error: (error) => this.handleError(error)
    });
  }

  searchProducts(): void {
    const term = this.searchTerm.toLowerCase();

    if (!term) {
      this.filteredProducts = [...this.products];
      this.noResults = this.filteredProducts.length === 0;
      this.groupProducts();
      return;
    }

    this.filteredProducts = this.products.filter(p =>
      p.name.toLowerCase().includes(term) ||
      p.description.toLowerCase().includes(term) ||
      (p.sku?.toLowerCase().includes(term) ?? false) ||
      p.company.name.toLowerCase().includes(term)
    );
    this.noResults = this.filteredProducts.length === 0;
    this.groupProducts();
  }

  private groupProducts(): void {
    this.groupedProducts = {};
    this.filteredProducts.forEach(p => {
      if (!this.groupedProducts[p.company.name]) {
        this.groupedProducts[p.company.name] = [];
      }
      this.groupedProducts[p.company.name].push(p);
    });
    this.groupedCompanyNames = Object.keys(this.groupedProducts);
  }

  openDetail(product: Product): void {
    this.selectedProduct = { ...product };
    this.detailVisible = true;
  }

  closeDetail(): void {
    this.selectedProduct = null;
    this.detailVisible = false;
  }

  goToCreate(): void {
    this.router.navigate(['/products/create']);
  }

  goToEdit(product: Product) {
    this.router.navigate([`/products/edit/${product.id}`]);
  }

  deleteProduct(product: Product) {
    this.selectedProduct = product;
    this.confirmMessage = `¿Está seguro de que desea eliminar el producto ${product.id} ${product.name}?`;
    this.showConfirmDelete = true;
  }

  confirmDelete(): void {
    if (!this.selectedProduct) return;

    this.productService.delete(this.selectedProduct.id).subscribe({
      next: () => this.loadProducts(),
      error: (error) => this.handleError(error),
      complete: () => {
        this.selectedProduct = null;
        this.showConfirmDelete = false;
        this.confirmMessage = '';
      }
    });
  }

  hasRole(roles: string[]): boolean {
    const userRoles = this.authService.getRoles();
    return roles.some(r => userRoles.includes(r));
  }


  cancelDelete(): void {
    this.selectedProduct = null;
    this.showConfirmDelete = false;
    this.confirmMessage = '';
  }

  private handleError(error: HttpErrorResponse): void {
    console.error('Error al cargar los productos:', error);
    this.errorMessage = error?.message || 'Error en la búsqueda de productos. Intente de nuevo.';
    this.errorVisible = true;
  }
}
