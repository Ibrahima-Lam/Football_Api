import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.token;
  let request = req;
  if (token) {
    request = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(request).pipe(
    catchError(err => {
      if (err.status === 401 && !req.url.includes('/api/auth/login')) {
        auth.handleUnauthorized();
      }
      return throwError(() => err);
    })
  );
};
