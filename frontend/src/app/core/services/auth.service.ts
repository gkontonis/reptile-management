import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthRequest, AuthResponse } from '../models/auth.model';

/**
 * Simplified user data stored in authentication state
 */
export interface AuthUser {
  username: string;
  roles: string[];
}

/**
 * Service for handling authentication operations.
 * Manages JWT tokens, user state, and authentication-related functionality using reactive signals.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = '/api/auth';
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'current_user';
  private readonly ROLE_ADMIN = 'ROLE_ADMIN';

  // Signals for reactive state management
  isAuthenticated = signal<boolean>(this.hasToken());
  currentUser = signal<AuthUser | null>(this.getStoredUser());

  // Computed signal for username (for backward compatibility)
  username = computed(() => this.currentUser()?.username ?? null);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  /**
   * Authenticates a user with the provided credentials.
   * Stores the JWT token and user information in localStorage and updates reactive state.
   * @param credentials - The login credentials
   * @returns Observable of AuthResponse containing token and user data
   */
  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          const user: AuthUser = {
            username: response.username,
            roles: response.roles
          };

          localStorage.setItem(this.TOKEN_KEY, response.token);
          localStorage.setItem(this.USER_KEY, JSON.stringify(user));
          this.isAuthenticated.set(true);
          this.currentUser.set(user);
        })
      );
  }

  /**
   * Logs out the current user.
   * Clears authentication data from localStorage and resets reactive state.
   * Redirects to the login page.
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.isAuthenticated.set(false);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  /**
   * Retrieves the stored JWT token.
   * @returns The JWT token string or null if not authenticated
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Check if the current user has admin role
   */
  isAdmin(): boolean {
    const user = this.currentUser();
    return user?.roles?.includes(this.ROLE_ADMIN) ?? false;
  }

  /**
   * Check if the current user has a specific role
   */
  hasRole(role: string): boolean {
    const user = this.currentUser();
    return user?.roles?.includes(role) ?? false;
  }

  /**
   * Checks if a valid token exists in localStorage.
   * @private
   * @returns True if a token exists, false otherwise
   */
  private hasToken(): boolean {
    return !!this.getToken();
  }

  /**
   * Retrieves the stored user data from localStorage.
   * Handles both new format (with roles) and legacy format (username only).
   * @private
   * @returns The parsed AuthUser object or null if not found
   */
  private getStoredUser(): AuthUser | null {
    const userJson = localStorage.getItem(this.USER_KEY);
    if (!userJson) {
      return null;
    }

    try {
      // Try to parse as JSON (new format with roles)
      return JSON.parse(userJson);
    } catch {
      // Fallback for old format (just username string)
      return {
        username: userJson,
        roles: []
      };
    }
  }
}
