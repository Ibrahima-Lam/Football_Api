import { HttpInterceptorFn } from '@angular/common/http';
import { timeout } from 'rxjs';

export const timeoutInterceptor: HttpInterceptorFn = (req, next) => {
  const isUpload = req.method === 'POST' && req.url.startsWith('/api/files');
  return next(req).pipe(timeout(isUpload ? 300000 : 15000));
};
