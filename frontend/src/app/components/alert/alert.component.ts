import { Component, EventEmitter, Input, Output } from '@angular/core';

type AlertType = 'success' | 'error' | 'info' | 'warning';

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrl: './alert.component.scss',
    standalone: false
})
export class AlertComponent {
  @Input() message: string = '';
  @Input() type: AlertType = 'info';
  @Input() visible: boolean = false;

 @Output() closed = new EventEmitter<void>();

  close() {
    this.visible = false;
    this.closed.emit();
  }

}
