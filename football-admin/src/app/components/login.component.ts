import { Component, inject, ElementRef, ViewChild, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  template: `
    <div class="login-wrapper">
      <div class="card shadow-lg border-0" style="max-width: 400px; width: 100%; border-radius: 1rem;">
        <div class="card-body p-4 p-md-5">
          <div class="text-center mb-4">
            <div class="login-icon mb-3">
              <i class="bi bi-shield-lock fs-1"></i>
            </div>
            <h4 class="mb-1 fw-bold">Football Admin</h4>
            <p class="text-secondary small mb-0">Sign in to access the administration panel</p>
          </div>
          <form (submit)="onSubmit($event)" novalidate>
            <div class="mb-3">
              <label class="form-label" for="email">Email <span class="text-danger">*</span></label>
              <input type="email" id="email" name="email" class="form-control" #emailInput
                placeholder="admin@football.com" autocomplete="username" [disabled]="loading()"
                [class.is-invalid]="submitted() && emailInvalid()" />
              @if (submitted() && emailInvalid()) {
                <div class="invalid-feedback">Please enter your email.</div>
              }
            </div>
            <div class="mb-4">
              <label class="form-label" for="password">Password <span class="text-danger">*</span></label>
              <div class="input-group">
                <input [type]="showPassword() ? 'text' : 'password'" id="password" name="password" class="form-control" #passwordInput
                  placeholder="••••••••" autocomplete="current-password" [disabled]="loading()"
                  [class.is-invalid]="submitted() && passwordInvalid()" />
                <button type="button" class="btn btn-outline-secondary" (click)="togglePassword()"
                  [attr.title]="showPassword() ? 'Hide password' : 'Show password'" [disabled]="loading()">
                  <i class="bi" [class.bi-eye]="!showPassword()" [class.bi-eye-slash]="showPassword()"></i>
                </button>
              </div>
              @if (submitted() && passwordInvalid()) {
                <div class="invalid-feedback">Password must be at least 6 characters.</div>
              }
            </div>
            @if (error()) {
              <div class="alert alert-danger py-2 small" role="alert">
                <i class="bi bi-exclamation-triangle me-1"></i>{{ error() }}
              </div>
            }
            <button type="submit" class="btn btn-dark w-100" [disabled]="loading()">
              @if (loading()) {
                <span class="spinner-border spinner-border-sm me-2"></span>Signing in...
              } @else {
                <i class="bi bi-box-arrow-in-right me-2"></i>Sign in
              }
            </button>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-wrapper {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #1c1f26 0%, #343a40 100%);
      padding: 1rem;
    }
    .login-icon {
      width: 72px;
      height: 72px;
      margin: 0 auto;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background: #212529;
      color: #fff;
    }
  `]
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  @ViewChild('emailInput') emailInput!: ElementRef<HTMLInputElement>;
  @ViewChild('passwordInput') passwordInput!: ElementRef<HTMLInputElement>;

  showPassword = signal(false);
  loading = signal(false);
  error = signal('');
  submitted = signal(false);
  emailInvalid = signal(false);
  passwordInvalid = signal(false);

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    if (this.loading()) {
      return;
    }
    const email = (this.emailInput?.nativeElement?.value ?? '').trim();
    const password = this.passwordInput?.nativeElement?.value ?? '';
    this.submitted.set(true);
    this.emailInvalid.set(!email);
    this.passwordInvalid.set(!password || password.length < 6);
    this.error.set('');

    if (this.emailInvalid() || this.passwordInvalid()) {
      this.error.set('Please enter a valid email and password.');
      return;
    }

    this.loading.set(true);
    this.auth.login(email, password).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.status === 401 ? 'Invalid email or password' : 'Login failed. Please try again.');
      }
    });
  }
}
