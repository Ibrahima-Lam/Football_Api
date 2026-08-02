import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, timeout } from 'rxjs';

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface FileResponse {
  id: string;
  originalName: string;
  fileName: string;
  category: string;
  contentType: string;
  size: number;
  url: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = '/api';

  constructor(private http: HttpClient) {}

  getAll<T>(resource: string, params?: any): Observable<PageResponse<T>> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] != null && params[key] !== '') {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }
    return this.http.get<PageResponse<T>>(`${this.baseUrl}/${resource}`, { params: httpParams }).pipe(timeout(15000));
  }

  getAllArray<T>(resource: string, params?: any): Observable<T[]> {
    return this.getAll<T>(resource, params).pipe(map(p => p.content));
  }

  getById<T>(resource: string, id: string): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}/${resource}/${id}`).pipe(timeout(15000));
  }

  create<T>(resource: string, body: any): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}/${resource}`, body).pipe(timeout(15000));
  }

  update<T>(resource: string, id: string, body: any): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}/${resource}/${id}`, body).pipe(timeout(15000));
  }

  delete(resource: string, id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${resource}/${id}`).pipe(timeout(15000));
  }

  upload(file: File): Observable<FileResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<FileResponse>(`${this.baseUrl}/files`, formData).pipe(timeout(300000));
  }
}
