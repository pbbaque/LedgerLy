import { Component, EventEmitter, Input, Output } from '@angular/core';

type ConfirmType = 'success' | 'error' | 'info' | 'warning';

@Component({
    selector: 'app-confirm',
    templateUrl: './confirm.component.html',
    styleUrls: ['./confirm.component.scss'],
    standalone: false
})
export class ConfirmComponent {
  @Input() message: string = '¿Estás seguro?';
  @Input() type: ConfirmType = 'info';
  @Input() visible: boolean = false;

  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  confirm() {
    this.visible = false;
    this.confirmed.emit();
  }

  cancel() {
    this.visible = false;
    this.cancelled.emit();
  }
}