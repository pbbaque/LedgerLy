import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-not-found',
    templateUrl: './not-found.component.html',
    styleUrl: './not-found.component.scss',
    standalone: false
})
export class NotFoundComponent {

  constructor(private router: Router) { }

  goHome(): void {
    this.router.navigate(['/dashboard']);
  }
}
