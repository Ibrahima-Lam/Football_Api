import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastService } from './services/toast.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <router-outlet></router-outlet>
    <div class="toast-container position-fixed bottom-0 end-0 p-3" style="z-index:9999">
      @for (toast of toastSvc.toasts(); track toast.id) {
        <div class="toast show align-items-center text-white border-0" [class.bg-success]="toast.type === 'success'"
          [class.bg-danger]="toast.type === 'danger'"
          [class.bg-warning]="toast.type === 'warning'"
          [class.bg-info]="toast.type === 'info'">
          <div class="d-flex">
            <div class="toast-body">
              <i class="bi" [class.bi-check-circle]="toast.type === 'success'"
                [class.bi-exclamation-triangle]="toast.type === 'danger' || toast.type === 'warning'"
                [class.bi-info-circle]="toast.type === 'info'"></i>
              {{ toast.message }}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" (click)="toastSvc.remove(toast.id)"></button>
          </div>
        </div>
      }
    </div>
  `
})
export class App {
  constructor(public toastSvc: ToastService) {}
}
