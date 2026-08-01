import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FcmService } from '../services/fcm.service';

@Component({
  selector: 'app-fcm-settings',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="card border-0 shadow-sm h-100">
      <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
        <h5 class="mb-0"><i class="bi bi-bell"></i> Notifications Push (FCM)</h5>
        @if (state.configured) {
          <span class="badge text-bg-success">Configuré</span>
        } @else {
          <span class="badge text-bg-secondary">Non configuré</span>
        }
      </div>
      <div class="card-body">
        @if (!state.configured) {
          <div class="alert alert-secondary mb-0">
            <i class="bi bi-info-circle me-1"></i>
            Firebase n'est pas configuré. Renseignez <code>public/firebase-config.json</code>
            et passez <code>enabled</code> à <code>true</code> pour activer les notifications push.
          </div>
        } @else {
          @if (state.loading) {
            <div class="text-center py-3">
              <div class="spinner-border spinner-border-sm text-primary" role="status"></div>
              <p class="text-muted small mt-2 mb-0">Initialisation...</p>
            </div>
          } @else {
            @if (state.error) {
              <div class="alert alert-danger py-2 small">
                <i class="bi bi-exclamation-triangle me-1"></i>{{ state.error }}
              </div>
            }
            <div class="d-flex align-items-center justify-content-between mb-2">
              <span class="text-muted small">Permission navigateur</span>
              <span class="badge"
                [class.text-bg-success]="state.permission === 'granted'"
                [class.text-bg-warning]="state.permission === 'default'"
                [class.text-bg-danger]="state.permission === 'denied'"
                [class.text-bg-secondary]="state.permission === 'unsupported'">
                {{ state.permission }}
              </span>
            </div>
            <div class="d-flex align-items-center justify-content-between mb-3">
              <span class="text-muted small">Token FCM</span>
              @if (state.token) {
                <span class="d-flex align-items-center gap-2">
                  <code class="small text-truncate fcm-token" [title]="state.token">{{ state.token }}</code>
                  <button class="btn btn-sm btn-outline-secondary py-0" (click)="fcm.copyToken()"
                    title="Copier le token">
                    <i class="bi bi-clipboard"></i>
                  </button>
                </span>
              } @else {
                <span class="badge text-bg-light border">Aucun</span>
              }
            </div>
            @if (state.token) {
              <button class="btn btn-outline-danger btn-sm w-100" (click)="fcm.disable()">
                <i class="bi bi-bell-slash me-1"></i> Désactiver les notifications
              </button>
            } @else {
              <button class="btn btn-primary btn-sm w-100" (click)="fcm.enable()"
                [disabled]="state.permission === 'denied'">
                <i class="bi bi-bell me-1"></i> Activer les notifications
              </button>
            }
          }
        }
      </div>
    </div>
  `,
  styles: [
    `.fcm-token { max-width: 180px; display: inline-block; }`,
    `code { direction: ltr; unicode-bidi: embed; }`
  ]
})
export class FcmSettingsComponent {
  constructor(public fcm: FcmService) {}

  get state() {
    return this.fcm.state();
  }
}
