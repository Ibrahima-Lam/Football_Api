import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { LiveService } from './services/live.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="d-flex flex-column min-vh-100">
    <nav class="fc-navbar navbar navbar-expand-lg sticky-top px-3">
      <div class="container-xxl">
        <a class="navbar-brand fc-brand d-flex align-items-center" routerLink="/">
          <i class="bi bi-lightning-charge-fill me-2"></i>
          FSCORE
        </a>
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#fcNav"
          aria-controls="fcNav" aria-expanded="false" aria-label="Menu">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="fcNav">
          <ul class="navbar-nav me-auto gap-1 mt-2 mt-lg-0">
            <li class="nav-item">
              <a class="nav-link" routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{ exact: true }">
                <i class="bi bi-calendar3 me-1"></i>Matchs
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/equipes" routerLinkActive="active">
                <i class="bi bi-shield me-1"></i>Équipes
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/actualites" routerLinkActive="active">
                <i class="bi bi-newspaper me-1"></i>Actualités
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/competitions" routerLinkActive="active">
                <i class="bi bi-trophy me-1"></i>Compétitions
              </a>
            </li>
          </ul>
          <div class="d-flex align-items-center gap-2 mt-2 mt-lg-0">
            @if (live.connected()) {
              <span class="fc-badge fc-badge-live"><span class="fc-live-dot"></span>Direct</span>
            }
            <a class="nav-link p-2" routerLink="/settings" routerLinkActive="active" title="Paramètres">
              <i class="bi bi-gear-fill fs-5"></i>
            </a>
          </div>
        </div>
      </div>
    </nav>

    <main class="container-xxl py-4 flex-grow-1">
      <router-outlet></router-outlet>
    </main>

    <footer class="fc-footer py-4">
      <div class="container-xxl d-flex flex-column flex-md-row justify-content-between align-items-center gap-2">
        <div class="d-flex align-items-center gap-2">
          <i class="bi bi-lightning-charge-fill"></i>
          <span class="fw-bold">FSCORE</span>
        </div>
        <div class="small">Scores, compétitions et statistiques en temps réel</div>
        <div class="d-flex gap-3 small">
          <a routerLink="/">Aujourd'hui</a>
          <a routerLink="/competitions">Compétitions</a>
          <a routerLink="/settings">Paramètres</a>
        </div>
      </div>
    </footer>
    </div>
  `,
})
export class App {
  live = inject(LiveService);

  constructor() {
    this.live.ensureConnected();
  }
}
