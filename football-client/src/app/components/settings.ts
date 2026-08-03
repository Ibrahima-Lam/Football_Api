import { Component, inject, signal } from '@angular/core';
import { ApiService } from '../services/api.service';
import { LiveService } from '../services/live.service';
import { SettingsService } from '../services/settings.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  template: `
    <div class="d-flex align-items-center gap-2 mb-4">
      <h1 class="h4 fw-bold mb-0"><i class="bi bi-gear me-2 text-primary"></i>Paramètres</h1>
    </div>

    <div class="row g-3">
      <div class="col-lg-7">
        <div class="fc-panel p-4">
          <div class="fc-section-title mb-3">Accès API</div>
          <p class="text-muted small">
            Cette application consomme l'API <strong>/api/client</strong>. L'accès est authentifié automatiquement
            avec une clé par défaut — aucune configuration requise.
          </p>

          <div class="mb-3">
            <label class="form-label fw-semibold">URL du serveur de direct (websocket)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-wifi"></i></span>
              <input class="form-control fc-input" type="text" placeholder="{{ defaultBroker }}"
                [value]="brokerUrl()" (input)="brokerUrl.set($any($event.target).value)" />
            </div>
            <div class="form-text">Laisser vide pour utiliser automatiquement l'hôte actuel (<code>/ws</code>).</div>
          </div>

          <div class="d-flex flex-wrap gap-2">
            <button class="btn btn-fc" (click)="save()"><i class="bi bi-check-lg me-1"></i>Enregistrer</button>
            <button class="btn btn-fc-ghost" (click)="test()" [disabled]="testing()">
              @if (testing()) {
                <span class="spinner-border spinner-border-sm me-1"></span>
              } @else {
                <i class="bi bi-plug me-1"></i>
              }
              Tester la connexion
            </button>
          </div>

          @if (saved()) {
            <div class="alert alert-success mt-3 mb-0 py-2">
              <i class="bi bi-check-circle me-1"></i>Paramètres enregistrés.
            </div>
          }
          @if (testResult(); as result) {
            <div class="alert mt-3 mb-0 py-2" [class.alert-success]="result.ok" [class.alert-danger]="!result.ok">
              @if (result.ok) {
                <i class="bi bi-check-circle me-1"></i>Connexion API réussie · {{ result.message }}
              } @else {
                <i class="bi bi-x-circle me-1"></i>{{ result.message }}
              }
            </div>
          }
        </div>
      </div>

      <div class="col-lg-5">
        <div class="fc-panel p-4 mb-3">
          <div class="fc-section-title mb-3">Connexion en direct</div>
          <div class="d-flex align-items-center gap-2">
            @if (live.connected()) {
              <span class="fc-badge fc-badge-live"><span class="fc-live-dot"></span>Connecté</span>
              <span class="small text-muted">Mises à jour en temps réel actives</span>
            } @else {
              <span class="fc-badge fc-badge-muted"><i class="bi bi-wifi-off"></i>Déconnecté</span>
              <button class="btn btn-sm btn-fc-ghost" (click)="live.ensureConnected()">Connecter</button>
            }
          </div>
        </div>

        <div class="fc-panel p-4">
          <div class="fc-section-title mb-3">Aide</div>
          <ul class="small text-muted mb-0 ps-3">
            <li>Le direct utilise STOMP sur <code>/ws</code> (canaux <code>/topic/live</code>, <code>/topic/events</code>, <code>/topic/standings</code>…).</li>
          </ul>
        </div>
      </div>
    </div>
  `,
})
export class SettingsComponent {
  private settings = inject(SettingsService);
  private api = inject(ApiService);
  live = inject(LiveService);

  brokerUrl = signal<string>(this.settings.brokerUrl());
  defaultBroker = this.guessBroker();
  saved = signal(false);
  testing = signal(false);
  testResult = signal<{ ok: boolean; message: string } | null>(null);

  private guessBroker(): string {
    const proto = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    return `${proto}//${window.location.host}/ws`;
  }

  save(): void {
    this.settings.setBrokerUrl(this.brokerUrl().trim());
    this.saved.set(true);
    setTimeout(() => this.saved.set(false), 3000);
  }

  test(): void {
    this.testing.set(true);
    this.testResult.set(null);
    this.api.competitions().subscribe({
      next: (list) => {
        this.testing.set(false);
        this.testResult.set({
          ok: true,
          message: `${list.length} compétition${list.length > 1 ? 's' : ''} récupérée${list.length > 1 ? 's' : ''}.`,
        });
      },
      error: () => {
        this.testing.set(false);
        this.testResult.set({ ok: false, message: 'Échec de la connexion. Vérifiez que le serveur est démarré.' });
      },
    });
  }
}
