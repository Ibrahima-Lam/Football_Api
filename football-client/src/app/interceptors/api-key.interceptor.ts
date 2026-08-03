import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { SettingsService } from '../services/settings.service';

export const apiKeyInterceptor: HttpInterceptorFn = (req, next) => {
  const settings = inject(SettingsService);
  const key = settings.apiKey;
  if (key && req.url.startsWith('/api/')) {
    return next(req.clone({ setHeaders: { 'X-Api-Key': key } }));
  }
  return next(req);
};
