import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { CompetitionRef } from '../models/models';
import { competitionLabel } from '../utils';

@Component({
  selector: 'app-competitions',
  standalone: true,
  template: `
    <div class="d-flex align-items-center gap-2 mb-4">
      <h1 class="h4 fw-bold mb-0"><i class="bi bi-trophy me-2 text-primary"></i>Compétitions</h1>
    </div>

    @if (error()) {
      <div class="alert alert-danger d-flex align-items-center gap-2">
        <i class="bi bi-exclamation-triangle-fill"></i>
        <div class="flex-grow-1">{{ error() }}</div>
        <button class="btn btn-sm btn-outline-danger" (click)="load()">Réessayer</button>
      </div>
    }

    @if (loading()) {
      <div class="fc-loading">
        <div class="spinner-border text-primary me-2" role="status"></div>
        Chargement des compétitions…
      </div>
    } @else {
      <div class="row g-3">
        @for (c of competitions(); track c.id) {
          <div class="col-12 col-sm-6 col-lg-4 col-xxl-3">
            <div class="fc-panel p-3 h-100 fc-card-hover" (click)="open(c)">
              <div class="d-flex align-items-center gap-3 mb-3">
                @if (c.logo) {
                  <img class="fc-crest fc-crest-lg" [src]="c.logo" [alt]="c.name" loading="lazy" />
                } @else {
                  <div class="fc-crest fc-crest-lg d-flex align-items-center justify-content-center text-primary">
                    <i class="bi bi-trophy fs-4"></i>
                  </div>
                }
                <div class="min-w-0 flex-grow-1">
                  <div class="fw-bold text-truncate">{{ competitionLabel(c) }}</div>
                  <div class="small text-muted">{{ c.countryName || c.confederationName || 'International' }}</div>
                </div>
                <i class="bi bi-chevron-right text-muted"></i>
              </div>

              <div class="d-flex flex-wrap align-items-center gap-1 mb-3">
                @if (c.type) {
                  <span class="fc-badge fc-badge-muted">{{ c.type }}</span>
                }
                @if (c.gender) {
                  <span class="fc-badge fc-badge-muted">{{ c.gender }}</span>
                }
              </div>

              <div class="d-flex align-items-center justify-content-between border-top pt-2">
                @if (c.currentSeason) {
                  <span class="small fw-semibold text-muted">
                    <i class="bi bi-calendar-event me-1"></i>{{ c.currentSeason.name }}
                  </span>
                  <span class="fc-badge" [class.fc-badge-live]="c.currentSeason.status === 'ACTIVE'"
                    [class.fc-badge-muted]="c.currentSeason.status !== 'ACTIVE'">
                    {{ c.currentSeason.status }}
                  </span>
                } @else {
                  <span class="small text-muted">Aucune saison active</span>
                }
              </div>
            </div>
          </div>
        }
      </div>

      @if (competitions().length === 0 && !loading()) {
        <div class="fc-panel text-center py-5 text-muted">
          <i class="bi bi-trophy display-6 d-block mb-2"></i>
          Aucune compétition disponible.
        </div>
      }
    }
  `,
})
export class CompetitionsComponent {
  private api = inject(ApiService);
  private router = inject(Router);

  competitions = signal<CompetitionRef[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  competitionLabel = competitionLabel;

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.api.competitions().subscribe({
      next: (list) => {
        this.competitions.set(list);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Impossible de charger les compétitions. Vérifiez votre clé API dans les paramètres.');
      },
    });
  }

  open(c: CompetitionRef): void {
    this.router.navigate(['/competition', c.id]);
  }
}
