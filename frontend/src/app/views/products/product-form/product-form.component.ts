import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Company } from '../../../models/company';
import { ProductService } from '../../../services/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CompanyService } from '../../../services/company.service';
import { Product } from '../../../models/product';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-product-form',
    templateUrl: './product-form.component.html',
    styleUrl: './product-form.component.scss',
    standalone: false
})
export class ProductFormComponent {
  errorMessage: string = '';
  errorVisible: boolean = false;
  productForm!: FormGroup;
  isEditMode: boolean = false;
  productId: number | null = null;
  companies: Company[] = [];
  isAdmin: boolean = false;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private companyService: CompanyService
  ) { }

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN') || this.authService.hasRole('ROLE_SUPER_ADMIN');

    this.productForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: ['', [Validators.required]],
      sku: ['', Validators.required],
      stock: ['', Validators.required],
      company: this.fb.group({
        id: [null, this.isAdmin ? Validators.required : null],
        name: ['']
      })
    });

    if (this.isAdmin)
      this.loadCompanies();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.productId = +id;
        this.loadProduct(this.productId);
      }
    });
  }

  loadProduct(id: number): void {
    this.productService.findById(id).subscribe({
      next: (product: Product) => {
        this.productForm.patchValue(product);
      },
      error: (error: HttpErrorResponse) => {        
        this.errorMessage = error.message || 'Error al cargar el producto';
        this.errorVisible = true;
      }
    });
  }

  loadCompanies(): void {
    this.companyService.findAll().subscribe({
      next: (companies: Company[]) => this.companies = companies,
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.message || 'Error al cargar las compañías';
        this.errorVisible = true;
      }
    });
  }

  onSubmit() {
    if (this.productForm.invalid)
      return;
    const productData: Product = this.productForm.value;
    productData.company.id = this.isAdmin ? this.productForm.value.company.id : this.authService.getCompanyId();
    if (this.isEditMode && this.productId) {
      productData.id = this.productId;
      this.productService.update(productData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al actualizar el producto';
          this.errorVisible = true;
        }
      });
    } else {
      this.productService.create(productData).subscribe({
        next: () => this.goBack(),
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message || 'Error al crear el producto';
          this.errorVisible = true;
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/products'])
  }

  get name() {
    return this.productForm.get('name');
  }

  get description() {
    return this.productForm.get('description');
  }

  get price() {
    return this.productForm.get('price');
  }

  get sku() {
    return this.productForm.get('sku');
  }

  get stock() {
    return this.productForm.get('stock');
  }

  get company() {
    return this.productForm.get('company');
  }
}
