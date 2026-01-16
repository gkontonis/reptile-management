import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * An HTTP interceptor function that attaches a JWT bearer token to outgoing HTTP requests,
 * except for requests targeting authentication endpoints (URLs containing '/auth/').
 * Also handles authentication errors by redirecting to login on 401/403 responses.
 * The token is retrieved from the injected AuthService.
 *
 * @param req - The outgoing HTTP request.
 * @param next - The next handler in the HTTP request chain.
 * @returns The handled HTTP request, potentially with an Authorization header set.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  // Clone request and add authorization header if token exists and not auth endpoint
  if (token && !req.url.includes('/auth/')) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Handle authentication errors
      if (error.status === 401 || error.status === 403) {
        // Clear authentication state and redirect to login
        authService.logout();
        return throwError(() => error);
      }

      // Re-throw other errors
      return throwError(() => error);
    })
  );
};
