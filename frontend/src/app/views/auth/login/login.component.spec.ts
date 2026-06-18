import { ComponentFixture, TestBed } from '@angular/core/testing';
import { componentTestImports, componentTestProviders, componentTestSchemas } from '../../../../test-support';

import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: componentTestImports,
      providers: componentTestProviders,
      schemas: componentTestSchemas,
      declarations: [LoginComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the login form with hidden password mode', () => {
    const passwordInput: HTMLInputElement = fixture.nativeElement.querySelector('#password');
    const toggleButton: HTMLButtonElement = fixture.nativeElement.querySelector('.password-field__toggle');

    expect(component.showPassword).toBeFalse();
    expect(passwordInput.type).toBe('password');
    expect(toggleButton.getAttribute('aria-label')).toBe('Mostrar contrasena');
  });

  it('should toggle password visibility from the eye button', () => {
    const toggleButton: HTMLButtonElement = fixture.nativeElement.querySelector('.password-field__toggle');

    toggleButton.click();
    fixture.detectChanges();

    let passwordInput: HTMLInputElement = fixture.nativeElement.querySelector('#password');
    expect(component.showPassword).toBeTrue();
    expect(passwordInput.type).toBe('text');
    expect(toggleButton.getAttribute('aria-label')).toBe('Ocultar contrasena');

    toggleButton.click();
    fixture.detectChanges();

    passwordInput = fixture.nativeElement.querySelector('#password');
    expect(component.showPassword).toBeFalse();
    expect(passwordInput.type).toBe('password');
  });
});
