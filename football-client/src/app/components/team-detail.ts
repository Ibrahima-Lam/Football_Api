import { Component, Input, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../services/api.service';
import { MatchListComponent } from './match-list';
import {
  MatchCard,
  NewsItem,
  PageInfo,
  PlayerSeasonStatItem,
  SquadPlayerItem,
  TeamDetail,
  TeamInjuryItem,
  TeamSuspensionItem,
} from '../models/models';
import { addDays, fmtDateFull, isoDate, todayIso } from '../utils';

type TeamTab = 'matches' | 'players' | 'stats' | 'suspensions' | 'injuries' | 'news';

@Component({
  selector: 'app-team-detail',
  standalone: true,
  imports: [RouterLink, MatchListComponent],
  template: `
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
        Chargement…
      </div>
    } @else if (team(); as t) {
      <a routerLink="/" class="text-muted text-decoration-none small fw-semibold d-inline-block mb-3">
        <i class="bi bi-arrow-left me-1"></i>Accueil
      </a>

      <section class="fc-hero p-4 mb-4">
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-3">
          @if (t.logo) {
            <img class="fc-crest fc-crest-lg" [src]="t.logo" [alt]="t.name" />
          } @else {
            <div class="fc-crest fc-crest-lg d-flex align-items-center justify-content-center text-white bg-white bg-opacity-10">
              <i class="bi bi-shield fs-3"></i>
            </div>
          }
          <div class="flex-grow-1">
            <h1 class="h4 fw-bold mb-1">{{ t.name }}</h1>
            <div class="small text-white-50">
              @if (t.countryFlag) {
                <img [src]="t.countryFlag" [alt]="t.countryIso2 ?? ''" style="width: 18px; height: 12px; object-fit: cover; border-radius: 2px" class="me-1" />
              }
              {{ t.countryName || t.countryIso2 || 'International' }}
              @if (t.founded) {
                <span class="mx-1">·</span><span>Fondé en {{ t.founded }}</span>
              }
            </div>
          </div>
        </div>
        <div class="d-flex flex-wrap gap-2 mt-3">
          @if (t.stadiumName) {
            <span class="fc-badge fc-badge-muted"><i class="bi bi-geo-alt me-1"></i>{{ t.stadiumName }}{{ t.stadiumCity ? ' · ' + t.stadiumCity : '' }}</span>
          }
          @if (t.stadiumCapacity) {
            <span class="fc-badge fc-badge-muted"><i class="bi bi-people me-1"></i>{{ t.stadiumCapacity.toLocaleString('fr-FR') }} places</span>
          }
          @if (t.website) {
            <span class="fc-badge fc-badge-muted"><i class="bi bi-globe me-1"></i>{{ t.website }}</span>
          }
        </div>
      </section>

      <div class="fc-tabs nav nav-pills gap-2 mb-3">
        <a class="nav-link" [class.active]="tab() === 'matches'" (click)="setTab('matches')">
          <i class="bi bi-calendar-week me-1"></i>Matchs
        </a>
        <a class="nav-link" [class.active]="tab() === 'players'" (click)="setTab('players')">
          <i class="bi bi-people me-1"></i>Joueurs
        </a>
        <a class="nav-link" [class.active]="tab() === 'stats'" (click)="setTab('stats')">
          <i class="bi bi-bar-chart me-1"></i>Statistiques
        </a>
        <a class="nav-link" [class.active]="tab() === 'suspensions'" (click)="setTab('suspensions')">
          <i class="bi bi-stop-octagon me-1"></i>Suspensions
        </a>
        <a class="nav-link" [class.active]="tab() === 'injuries'" (click)="setTab('injuries')">
          <i class="bi bi-heart-pulse me-1"></i>Blessures
        </a>
        <a class="nav-link" [class.active]="tab() === 'news'" (click)="setTab('news')">
          <i class="bi bi-newspaper me-1"></i>Actualités
        </a>
      </div>

      @if (tab() === 'matches') {
        <div class="d-flex flex-wrap align-items-center gap-2 mb-3">
          <button class="fc-date-btn" (click)="prevDay()"><i class="bi bi-chevron-left"></i></button>
          <span class="fw-bold">{{ fmtDateFull(matchDate() + 'T12:00:00') }}</span>
          <button class="fc-date-btn" (click)="nextDay()"><i class="bi bi-chevron-right"></i></button>
          <button class="fc-date-btn" (click)="matchDate.set(todayIso()); loadMatches()">Aujourd'hui</button>
          <input type="date" class="form-control fc-input" style="max-width: 160px" [value]="matchDate()"
            (change)="onMatchDateChange($event)" />
        </div>

        @if (matchesLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des matchs…
          </div>
        } @else {
          <app-match-list [matches]="matches()" />
        }
      }

      @if (tab() === 'players') {
        @if (playersLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des joueurs…
          </div>
        } @else if (players().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-people display-6 d-block mb-2"></i>
            Aucun joueur enregistré pour cette équipe.
          </div>
        } @else {
          <div class="row g-3">
            @for (p of players(); track p.id) {
              <div class="col-6 col-md-4 col-lg-3 col-xxl-2">
                <div class="fc-panel p-3 h-100 d-flex flex-column align-items-center text-center">
                  @if (p.photo) {
                    <img class="fc-crest fc-crest-lg mx-auto mb-2" [src]="p.photo" [alt]="p.fullName" loading="lazy" />
                  } @else {
                    <div class="fc-crest fc-crest-lg mx-auto mb-2 d-flex align-items-center justify-content-center text-muted bg-body-tertiary">
                      <i class="bi bi-person"></i>
                    </div>
                  }
                  <div class="fw-bold small text-truncate w-100">
                    @if (p.captain) {
                      <i class="bi bi-star-fill text-warning me-1" title="Capitaine"></i>
                    }
                    {{ p.fullName }}
                  </div>
                  <div class="small text-muted mt-1">
                    @if (p.shirtNumber) {
                      <span class="fc-badge fc-badge-muted me-1">#{{ p.shirtNumber }}</span>
                    }
                    {{ positionLabel(p.position) }}
                  </div>
                  @if (p.nationalityFlag) {
                    <img class="mt-1" [src]="p.nationalityFlag" [alt]="p.nationalityName ?? ''" style="width: 18px; height: 12px; object-fit: cover; border-radius: 2px" loading="lazy" />
                  }
                </div>
              </div>
            }
          </div>
        }
      }

      @if (tab() === 'stats') {
        @if (statsLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des statistiques…
          </div>
        } @else if (stats().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-bar-chart display-6 d-block mb-2"></i>
            Aucune statistique disponible.
          </div>
        } @else {
          <div class="fc-panel overflow-auto">
            <table class="table fc-table align-middle mb-0">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Joueur</th>
                  <th class="text-center">MJ</th>
                  <th class="text-center">Tit.</th>
                  <th class="text-center">Buts</th>
                  <th class="text-center">Passes</th>
                  <th class="text-center">J.</th>
                  <th class="text-center">R.</th>
                  <th class="text-center">Note</th>
                </tr>
              </thead>
              <tbody>
                @for (s of stats(); track s.player.id) {
                  <tr>
                    <td class="text-muted">{{ $index + 1 }}</td>
                    <td>
                      <div class="d-flex align-items-center gap-2">
                        @if (s.player.photo) {
                          <img class="fc-crest-sm" [src]="s.player.photo" [alt]="s.player.fullName" loading="lazy" />
                        } @else {
                          <div class="fc-crest-sm d-flex align-items-center justify-content-center text-muted bg-body-tertiary">
                            <i class="bi bi-person"></i>
                          </div>
                        }
                        <span class="fw-semibold">{{ s.player.fullName }}</span>
                      </div>
                    </td>
                    <td class="text-center">{{ s.appearances }}</td>
                    <td class="text-center">{{ s.appearancesAsStarter }}</td>
                    <td class="text-center fw-bold">{{ s.goals }}</td>
                    <td class="text-center">{{ s.assists }}</td>
                    <td class="text-center text-warning">{{ s.yellowCards }}</td>
                    <td class="text-center text-danger">{{ s.redCards }}</td>
                    <td class="text-center">{{ s.avgRating != null ? s.avgRating : '—' }}</td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        }
      }

      @if (tab() === 'suspensions') {
        @if (suspensionsLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des suspensions…
          </div>
        } @else if (suspensions().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-stop-octagon display-6 d-block mb-2"></i>
            Aucune suspension pour cette équipe.
          </div>
        } @else {
          <div class="row g-3">
            @for (s of suspensions(); track s.id) {
              <div class="col-md-6">
                <div class="fc-panel p-3 d-flex gap-3 h-100">
                  @if (s.player.photo) {
                    <img class="fc-crest" [src]="s.player.photo" [alt]="s.player.fullName" loading="lazy" />
                  } @else {
                    <div class="fc-crest d-flex align-items-center justify-content-center text-muted bg-body-tertiary">
                      <i class="bi bi-person"></i>
                    </div>
                  }
                  <div class="flex-grow-1 fc-min-w-0">
                    <div class="d-flex flex-wrap align-items-center gap-2">
                      <span class="fw-bold">{{ s.player.fullName }}</span>
                      <span class="fc-badge" [class]="cardBadgeClass(s.cardType)">
                        {{ cardLabel(s.cardType) }}
                      </span>
                      @if (s.matchesRemaining && s.matchesRemaining > 0) {
                        <span class="fc-badge fc-badge-live">{{ s.matchesRemaining }} match{{ s.matchesRemaining > 1 ? 's' : '' }} restant{{ s.matchesRemaining > 1 ? 's' : '' }}</span>
                      }
                    </div>
                    @if (s.reason) {
                      <div class="small text-muted mt-1">{{ s.reason }}</div>
                    }
                    <div class="small text-muted mt-1">
                      {{ fmtDay(s.startDate) }} @if (s.endDate) { → {{ fmtDay(s.endDate) }} }
                    </div>
                    <div class="d-flex flex-wrap gap-2 mt-2">
                      @if (s.competitionName) {
                        <span class="fc-badge fc-badge-muted"><i class="bi bi-trophy me-1"></i>{{ s.competitionName }}</span>
                      }
                      @if (s.seasonName) {
                        <span class="fc-badge fc-badge-muted">{{ s.seasonName }}</span>
                      }
                      <span class="fc-badge fc-badge-muted">{{ statusLabel(s.status) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            }
          </div>
        }
      }

      @if (tab() === 'injuries') {
        @if (injuriesLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des blessures…
          </div>
        } @else if (injuries().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-heart-pulse display-6 d-block mb-2"></i>
            Aucune blessure enregistrée pour cette équipe.
          </div>
        } @else {
          <div class="row g-3">
            @for (i of injuries(); track i.id) {
              <div class="col-md-6">
                <div class="fc-panel p-3 d-flex gap-3 h-100">
                  @if (i.player.photo) {
                    <img class="fc-crest" [src]="i.player.photo" [alt]="i.player.fullName" loading="lazy" />
                  } @else {
                    <div class="fc-crest d-flex align-items-center justify-content-center text-muted bg-body-tertiary">
                      <i class="bi bi-person"></i>
                    </div>
                  }
                  <div class="flex-grow-1 fc-min-w-0">
                    <div class="d-flex flex-wrap align-items-center gap-2">
                      <span class="fw-bold">{{ i.player.fullName }}</span>
                      <span class="fc-badge" [class]="severityBadgeClass(i.severity)">{{ severityLabel(i.severity) }}</span>
                    </div>
                    <div class="small mt-1">
                      <i class="bi bi-bandage text-muted me-1"></i>{{ i.injuryType }}
                      @if (i.bodyPart) {
                        <span class="text-muted"> · {{ i.bodyPart }}</span>
                      }
                    </div>
                    <div class="small text-muted mt-1">Depuis le {{ fmtDay(i.startDate) }}</div>
                    <div class="d-flex flex-wrap gap-2 mt-2">
                      @if (i.expectedReturn) {
                        <span class="fc-badge fc-badge-muted"><i class="bi bi-calendar2-week me-1"></i>Retour prévu {{ fmtDay(i.expectedReturn) }}</span>
                      }
                      <span class="fc-badge fc-badge-muted">{{ statusLabel(i.status) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            }
          </div>
        }
      }

      @if (tab() === 'news') {
        @if (newsLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des actualités…
          </div>
        } @else if (news().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-newspaper display-6 d-block mb-2"></i>
            Aucune actualité pour cette équipe.
          </div>
        } @else {
          <div class="row g-3">
            @for (n of news(); track n.id) {
              <div class="col-md-6 col-lg-4">
                <div class="fc-panel h-100 d-flex flex-column overflow-hidden">
                  @if (n.image) {
                    <img [src]="n.image" [alt]="n.title" class="w-100" style="height: 150px; object-fit: cover" loading="lazy" />
                  }
                  <div class="p-3 d-flex flex-column flex-grow-1">
                    <div class="small text-muted mb-1">
                      @if (n.competitionName) {
                        <span class="fc-badge fc-badge-muted me-1">{{ n.competitionName }}</span>
                      }
                      {{ fmtNewsDate(n.publishedAt) }}
                    </div>
                    <div class="fw-bold mb-1">{{ n.title }}</div>
                    @if (n.excerpt) {
                      <p class="small text-muted mb-2 flex-grow-1">{{ n.excerpt }}</p>
                    }
                    @if (n.author) {
                      <div class="small text-muted fw-semibold">{{ n.author }}</div>
                    }
                  </div>
                </div>
              </div>
            }
          </div>
        }
      }
    }
  `,
})
export class TeamDetailComponent implements OnInit {
  @Input() id!: string;

  private api = inject(ApiService);

  team = signal<TeamDetail | null>(null);
  tab = signal<TeamTab>('matches');
  matches = signal<MatchCard[]>([]);
  news = signal<NewsItem[]>([]);
  players = signal<SquadPlayerItem[]>([]);
  stats = signal<PlayerSeasonStatItem[]>([]);
  suspensions = signal<TeamSuspensionItem[]>([]);
  injuries = signal<TeamInjuryItem[]>([]);
  matchDate = signal(todayIso());
  loading = signal(true);
  matchesLoading = signal(true);
  newsLoading = signal(false);
  playersLoading = signal(false);
  statsLoading = signal(false);
  suspensionsLoading = signal(false);
  injuriesLoading = signal(false);
  error = signal<string | null>(null);

  private playersLoaded = false;
  private statsLoaded = false;
  private suspensionsLoaded = false;
  private injuriesLoaded = false;

  todayIso = todayIso;
  fmtDateFull = fmtDateFull;

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.api.teamDetail(this.id).subscribe({
      next: (team) => {
        this.team.set(team);
        this.loading.set(false);
        this.loadMatches();
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Impossible de charger cette équipe.');
      },
    });
  }

  setTab(tab: TeamTab): void {
    this.tab.set(tab);
    switch (tab) {
      case 'news':
        this.loadNews();
        break;
      case 'players':
        this.loadPlayers();
        break;
      case 'stats':
        this.loadStats();
        break;
      case 'suspensions':
        this.loadSuspensions();
        break;
      case 'injuries':
        this.loadInjuries();
        break;
    }
  }

  prevDay(): void {
    this.setMatchDate(isoDate(addDays(new Date(this.matchDate() + 'T12:00:00'), -1)));
  }

  nextDay(): void {
    this.setMatchDate(isoDate(addDays(new Date(this.matchDate() + 'T12:00:00'), 1)));
  }

  onMatchDateChange(event: Event): void {
    this.setMatchDate((event.target as HTMLInputElement).value);
  }

  private setMatchDate(iso: string): void {
    if (!iso) return;
    this.matchDate.set(iso);
    this.loadMatches();
  }

  loadMatches(): void {
    this.matchesLoading.set(true);
    this.api
      .matches({ date: this.matchDate(), teamId: this.id, size: 100 })
      .subscribe({
        next: (pageInfo: PageInfo<MatchCard>) => {
          this.matches.set(pageInfo.content);
          this.matchesLoading.set(false);
        },
        error: () => {
          this.matches.set([]);
          this.matchesLoading.set(false);
        },
      });
  }

  loadNews(): void {
    this.newsLoading.set(true);
    this.api.news(undefined, this.id).subscribe({
      next: (rows) => {
        this.news.set(rows);
        this.newsLoading.set(false);
      },
      error: () => {
        this.news.set([]);
        this.newsLoading.set(false);
      },
    });
  }

  loadPlayers(): void {
    if (this.playersLoaded) return;
    this.playersLoading.set(true);
    this.api.teamPlayers(this.id).subscribe({
      next: (rows) => {
        this.players.set(rows);
        this.playersLoaded = true;
        this.playersLoading.set(false);
      },
      error: () => {
        this.players.set([]);
        this.playersLoading.set(false);
      },
    });
  }

  loadStats(): void {
    if (this.statsLoaded) return;
    this.statsLoading.set(true);
    this.api.teamStats(this.id).subscribe({
      next: (rows) => {
        this.stats.set(rows);
        this.statsLoaded = true;
        this.statsLoading.set(false);
      },
      error: () => {
        this.stats.set([]);
        this.statsLoading.set(false);
      },
    });
  }

  loadSuspensions(): void {
    if (this.suspensionsLoaded) return;
    this.suspensionsLoading.set(true);
    this.api.teamSuspensions(this.id).subscribe({
      next: (rows) => {
        this.suspensions.set(rows);
        this.suspensionsLoaded = true;
        this.suspensionsLoading.set(false);
      },
      error: () => {
        this.suspensions.set([]);
        this.suspensionsLoading.set(false);
      },
    });
  }

  loadInjuries(): void {
    if (this.injuriesLoaded) return;
    this.injuriesLoading.set(true);
    this.api.teamInjuries(this.id).subscribe({
      next: (rows) => {
        this.injuries.set(rows);
        this.injuriesLoaded = true;
        this.injuriesLoading.set(false);
      },
      error: () => {
        this.injuries.set([]);
        this.injuriesLoading.set(false);
      },
    });
  }

  positionLabel(position: string): string {
    switch (position?.toUpperCase()) {
      case 'GK':
        return 'Gardien';
      case 'DF':
      case 'DEFENDER':
        return 'Défenseur';
      case 'MF':
      case 'MIDFIELDER':
        return 'Milieu';
      case 'FW':
      case 'FORWARD':
      case 'STRIKER':
        return 'Attaquant';
      default:
        return position || '—';
    }
  }

  cardLabel(cardType: string): string {
    switch (cardType?.toUpperCase()) {
      case 'YELLOW':
      case 'YELLOW_CARD':
        return 'Carton jaune';
      case 'RED':
      case 'RED_CARD':
        return 'Carton rouge';
      case 'YELLOW_RED':
        return 'Double carton';
      default:
        return cardType || 'Suspension';
    }
  }

  cardBadgeClass(cardType: string): string {
    switch (cardType?.toUpperCase()) {
      case 'YELLOW':
      case 'YELLOW_CARD':
        return 'fc-badge-scheduled';
      case 'RED':
      case 'RED_CARD':
        return 'fc-badge-postponed';
      default:
        return 'fc-badge-muted';
    }
  }

  severityLabel(severity: string): string {
    switch (severity?.toUpperCase()) {
      case 'MINOR':
        return 'Mineur';
      case 'MODERATE':
        return 'Modéré';
      case 'SEVERE':
        return 'Sévère';
      case 'SEASON_ENDING':
        return 'Fin de saison';
      default:
        return severity || '—';
    }
  }

  severityBadgeClass(severity: string): string {
    switch (severity?.toUpperCase()) {
      case 'MINOR':
        return 'fc-badge-scheduled';
      case 'MODERATE':
        return 'fc-badge-postponed';
      case 'SEVERE':
      case 'SEASON_ENDING':
        return 'fc-badge-live';
      default:
        return 'fc-badge-muted';
    }
  }

  statusLabel(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE':
        return 'En cours';
      case 'SERVED':
        return 'Purge effectuée';
      case 'RECOVERED':
        return 'Rétabli';
      case 'COMPLETED':
        return 'Terminé';
      default:
        return status || '—';
    }
  }

  fmtDay(value: string): string {
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? '' : d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' });
  }

  fmtNewsDate(value: string): string {
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? '' : d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' });
  }
}
