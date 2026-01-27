import { Component, signal, ChangeDetectionStrategy } from '@angular/core';
import { AuthRequest } from '../../../core/models/auth.model';
import { AuthService } from '../../../core/services/auth.service';
import { SanitizationService } from '../../../core/services/sanitization.service';
import { Router } from '@angular/router';

import { FormsModule } from '@angular/forms';

/**
 * Login component for user authentication.
 * Handles user login form submission, input sanitization, and navigation after successful authentication.
 * Uses reactive signals for loading and error state management.
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent {
  /** Login form data bound to template inputs */
  credentials: AuthRequest = {
    username: '',
    password: '',
  };

  /** Loading state signal for UI feedback during authentication */
  loading = signal(false);
  /** Error message signal for displaying authentication failures */
  errorMessage = signal<string | null>(null);

  constructor(
    private authService: AuthService,
    private sanitizationService: SanitizationService,
    private router: Router
  ) {}

  /**
   * Handles form submission for user login.
   * Sanitizes username input, performs authentication, and handles success/error responses.
   * On success, navigates to dashboard. On failure, displays error message.
   */
  onSubmit(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    // Sanitize inputs before sending to server
    const sanitizedCredentials: AuthRequest = {
      username: this.sanitizationService.sanitizeUsername(this.credentials.username),
      password: this.credentials.password // Password sanitization is handled by the server
    };

    this.authService.login(sanitizedCredentials).subscribe({
      next: () => {
        this.router.navigate(['/reptiles']);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set('Invalid username or password');
        console.error('Login error:', error);
      },
    });
  }
}
