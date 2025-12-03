import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DetailConfig } from '../../models/detail-config';
import { Address } from '../../models/address';
import { AddressService } from '../../services/address.service';

@Component({
    selector: 'app-entity-detail',
    templateUrl: './entity-detail.component.html',
    styleUrl: './entity-detail.component.scss',
    standalone: false
})
export class EntityDetailComponent {
  @Input() entity: any;
  @Input() config!: DetailConfig;
  @Input() visible: boolean = false;
  @Output() closed = new EventEmitter<void>();

  constructor(
    private addressService: AddressService
  ) { }

  getValueByPath(obj: any, path: string): any {
    const value = path.split('.').reduce((acc, part) => acc && acc[part], obj);
    if (value === undefined || value === null) {
      return "-";
    }

    if (path.toLowerCase().includes('address') && typeof value === 'object') {
      return this.formatAddress(value as Address);
    }

    return value;
  }

  onCloseButtonClick(): void {
    this.closed.emit();
  }

  onOverlayClick(): void {
    this.closed.emit();
  }

  formatAddress(address: Address): string {
    return this.addressService.formatAddress(address);
  }
}
