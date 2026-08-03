import { Component, OnDestroy, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiService } from '../services/api.service';
import { LiveService } from '../services/live.service';
import { CompetitionRef, LiveScoreMessage, MatchCard, NewsItem, PageInfo, SeasonRef, TeamRef } from '../models/models';
import { MatchListComponent } from './match-list';
import { addDays, fmtDate, isoDate, todayIso } from '../utils';

interface Group {
  id: string;
  competition: CompetitionRef;
  matches: MatchCard[];
}

type HomeTab = 'matches' | 'teams' | 'news';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatchListComponent],
  template: `
    <section class="fc-hero p-4 mb-4">
      <div class="mb-3">
        <h1 class="h4 fw-bold mb-1">
          <i class="bi bi-lightning-charge-fill me-2"></i>FSCORE
        </h1>
        <div class="text-white-50 small">Scores, équipes, compétitions et actualités en temps réel</div>
      </div>

      <div class="d-flex flex-wrap align-items-center gap-2 pt-3" style="border-top: 1px solid rgba(255,255,255,.18)">
        <select class="form-select fc-select fc-select-hero" style="max-width: 300px" [value]="competitionId() ?? ''"
          (change)="onCompetitionChange($event)">
          <option value="">Toutes les compétitions</option>
          @for (c of competitions(); track c.id) {
            <option [value]="c.id">{{ c.name }}</option>
          }
        </select>
        @if (tab() === 'matches') {
          @if (liveOnly()) {
            <span class="fc-live-hint me-1"><span class="fc-live-dot me-1"></span>Matchs en cours</span>
          } @else {
            <button class="fc-date-btn fc-date-btn-hero" (click)="prevDay()" title="Jour précédent"><i class="bi bi-chevron-left"></i></button>
            <div class="fw-bold" style="min-width: 190px; text-align: center">{{ fmtDate(selectedDate() + 'T12:00:00') }}</div>
            <button class="fc-date-btn fc-date-btn-hero" (click)="nextDay()" title="Jour suivant"><i class="bi bi-chevron-right"></i></button>
            <button class="fc-date-btn fc-date-btn-hero" [class.fc-today]="isToday()" (click)="goToday()">Aujourd'hui</button>
            <input type="date" class="form-control fc-input" style="max-width: 160px" [value]="selectedDate()"
              (change)="onDateChange($event)" />
          }
          @if (competitionId()) {
            <select class="form-select fc-select fc-select-hero" style="max-width: 240px" [value]="seasonId() ?? ''"
              (change)="onSeasonChange($event)">
              <option value="">Toutes les saisons</option>
              @for (s of seasons(); track s.id) {
                <option [value]="s.id">{{ s.name }} @if (s.current) { (actuelle) }</option>
              }
            </select>
          }
          <button class="fc-date-btn fc-date-btn-hero fc-live-toggle" [class.active]="liveOnly()" (click)="toggleLive()" title="Afficher uniquement les matchs en cours">
            <i class="bi bi-broadcast me-1"></i>En direct
          </button>
        }
      </div>
    </section>

    @if (tab() === 'matches') {
      @if (error()) {
        <div class="alert alert-danger d-flex align-items-center gap-2">
          <i class="bi bi-exclamation-triangle-fill"></i>
          <div class="flex-grow-1">{{ error() }}</div>
          <button class="btn btn-sm btn-outline-danger" (click)="loadMatches()">Réessayer</button>
        </div>
      }

      @if (loading()) {
        <div class="fc-loading">
          <div class="spinner-border text-primary me-2" role="status"></div>
          Chargement des matchs…
        </div>
      } @else {
        @for (group of groups(); track group.id) {
          <div class="fc-section-title mt-4 mb-3">
            @if (group.competition.logo) {
              <img class="fc-crest-sm" [src]="group.competition.logo" [alt]="group.competition.name" loading="lazy" />
            } @else {
              <i class="bi bi-trophy text-primary"></i>
            }
            <span>{{ group.competition.name }}</span>
            @if (group.competition.type) {
              <span class="fc-badge fc-badge-muted">{{ group.competition.type }}</span>
            }
          </div>
          <app-match-list [matches]="group.matches" />
        }

        @if (groups().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-broadcast display-6 d-block mb-2"></i>
            <div class="fw-semibold">{{ liveOnly() ? 'Aucun match en cours' : 'Aucun match pour ce jour' }}</div>
            <div class="small">{{ liveOnly() ? 'Revenez un peu plus tard.' : 'Essayez une autre date ou modifiez les filtres.' }}</div>
          </div>
        }

        @if (pageInfo() && pageInfo()!.totalPages > 1) {
          <nav class="mt-4" aria-label="Pagination">
            <ul class="pagination justify-content-center">
              <li class="page-item" [class.disabled]="pageInfo()!.first || page() === 0">
                <button class="page-link" (click)="changePage(page() - 1)">Précédent</button>
              </li>
              @for (p of pageNumbers(); track p) {
                <li class="page-item" [class.active]="p === page()">
                  <button class="page-link" (click)="changePage(p)">{{ p + 1 }}</button>
                </li>
              }
              <li class="page-item" [class.disabled]="pageInfo()!.last">
                <button class="page-link" (click)="changePage(page() + 1)">Suivant</button>
              </li>
            </ul>
          </nav>
        }
      }
    }

    @if (tab() === 'teams') {
      @if (teamsError()) {
        <div class="alert alert-danger d-flex align-items-center gap-2">
          <i class="bi bi-exclamation-triangle-fill"></i>
          <div class="flex-grow-1">{{ teamsError() }}</div>
          <button class="btn btn-sm btn-outline-danger" (click)="loadTeams()">Réessayer</button>
        </div>
      }

      @if (teamsLoading()) {
        <div class="fc-loading">
          <div class="spinner-border text-primary me-2" role="status"></div>
          Chargement des équipes…
        </div>
      } @else if (teams().length === 0) {
        <div class="fc-panel text-center py-5 text-muted">
          <i class="bi bi-shield display-6 d-block mb-2"></i>
          Aucune équipe disponible.
        </div>
      } @else {
        <div class="row g-3">
          @for (t of teams(); track t.id) {
            <div class="col-6 col-md-4 col-lg-3 col-xxl-2">
              <div class="fc-panel p-3 h-100 fc-card-hover text-center" (click)="openTeam(t)">
                @if (t.logo) {
                  <img class="fc-crest fc-crest-lg mx-auto mb-2" [src]="t.logo" [alt]="t.name" loading="lazy" />
                } @else {
                  <div class="fc-crest fc-crest-lg mx-auto mb-2 d-flex align-items-center justify-content-center text-muted bg-body-tertiary">
                    <i class="bi bi-shield"></i>
                  </div>
                }
                <div class="fw-bold small text-truncate">{{ t.name }}</div>
                @if (t.countryFlag) {
                  <img class="mt-1" [src]="t.countryFlag" [alt]="t.countryIso2 ?? ''" style="width: 18px; height: 12px; object-fit: cover; border-radius: 2px" loading="lazy" />
                } @else {
                  <div class="small text-muted">{{ t.countryIso2 ?? '' }}</div>
                }
              </div>
            </div>
          }
        </div>
      }
    }

    @if (tab() === 'news') {
      @if (newsError()) {
        <div class="alert alert-danger d-flex align-items-center gap-2">
          <i class="bi bi-exclamation-triangle-fill"></i>
          <div class="flex-grow-1">{{ newsError() }}</div>
          <button class="btn btn-sm btn-outline-danger" (click)="loadNews()">Réessayer</button>
        </div>
      }

      @if (newsLoading()) {
        <div class="fc-loading">
          <div class="spinner-border text-primary me-2" role="status"></div>
          Chargement des actualités…
        </div>
      } @else if (news().length === 0) {
        <div class="fc-panel text-center py-5 text-muted">
          <i class="bi bi-newspaper display-6 d-block mb-2"></i>
          Aucune actualité disponible.
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
                  <div class="small text-muted">
                    @if (n.author) {
                      <span class="fw-semibold">{{ n.author }}</span>
                    }
                    @if (n.team) {
                      <span> · {{ n.team.name }}</span>
                    }
                  </div>
                </div>
              </div>
            </div>
          }
        </div>
      }
    }
  `,
})
export class HomeComponent implements OnDestroy {
  private api = inject(ApiService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private live = inject(LiveService);
  private subscriptions: Subscription[] = [];

  tab = signal<HomeTab>('matches');
  selectedDate = signal(todayIso());
  isToday = signal(true);
  liveOnly = signal(false);
  competitionId = signal<string | null>(null);
  seasonId = signal<string | null>(null);
  competitions = signal<CompetitionRef[]>([]);
  seasons = signal<SeasonRef[]>([]);
  teams = signal<TeamRef[]>([]);
  pageInfo = signal<PageInfo<MatchCard> | null>(null);
  page = signal(0);
  loading = signal(true);
  error = signal<string | null>(null);
  teamsLoading = signal(false);
  teamsError = signal<string | null>(null);
  news = signal<NewsItem[]>([]);
  newsLoading = signal(false);
  newsError = signal<string | null>(null);

  fmtDate = fmtDate;

  readonly groups = computed<Group[]>(() => {
    const map = new Map<string, Group>();
    for (const m of this.pageInfo()?.content ?? []) {
      const id = m.competition?.id ?? 'other';
      let group = map.get(id);
      if (!group) {
        group = { id, competition: m.competition, matches: [] };
        map.set(id, group);
      }
      group.matches.push(m);
    }
    return [...map.values()];
  });

  readonly pageNumbers = computed<number[]>(() => {
    const total = this.pageInfo()?.totalPages ?? 1;
    const start = Math.max(0, this.page() - 2);
    const end = Math.min(total, start + 5);
    return Array.from({ length: Math.max(0, end - start) }, (_, i) => start + i);
  });

  constructor() {
    const initialTab = (this.route.snapshot.data['tab'] as HomeTab) ?? 'matches';
    this.tab.set(initialTab);

    this.api.competitions().subscribe({
      next: (list) => {
        this.competitions.set(list);
        if (this.competitionId()) {
          this.loadSeasons(this.competitionId()!);
        }
      },
    });

    if (initialTab === 'news') {
      this.loadNews();
    } else if (initialTab === 'teams') {
      this.loadTeams();
    } else {
      this.loadTeams();
      this.loadMatches();
    }

    this.subscriptions.push(
      this.live.subscribe<LiveScoreMessage>('/topic/live').subscribe((msg) => this.applyLiveUpdate(msg)),
    );
  }

  ngOnDestroy(): void {
    for (const sub of this.subscriptions) {
      sub.unsubscribe();
    }
    this.subscriptions = [];
  }

  private applyLiveUpdate(msg: LiveScoreMessage): void {
    const info = this.pageInfo();
    if (!info) return;
    const idx = info.content.findIndex((m) => m.id === msg.matchId);
    if (idx < 0) return;
    const current = info.content[idx];
    const updated: MatchCard = {
      ...current,
      status: msg.status,
      period: msg.period,
      minute: msg.minute,
      minuteExtra: msg.minuteExtra,
      homeScore: msg.homeScore,
      awayScore: msg.awayScore,
      homeHtScore: msg.homeHtScore,
      awayHtScore: msg.awayHtScore,
      homeEtScore: msg.homeEtScore,
      awayEtScore: msg.awayEtScore,
      homePenaltyScore: msg.homePenaltyScore,
      awayPenaltyScore: msg.awayPenaltyScore,
    };
    const content = [...info.content];
    content[idx] = updated;
    this.pageInfo.set({ ...info, content });
  }

  prevDay(): void {
    this.setDate(isoDate(addDays(new Date(this.selectedDate() + 'T12:00:00'), -1)));
  }

  nextDay(): void {
    this.setDate(isoDate(addDays(new Date(this.selectedDate() + 'T12:00:00'), 1)));
  }

  goToday(): void {
    this.setDate(todayIso());
  }

  toggleLive(): void {
    this.liveOnly.update((v) => !v);
    this.page.set(0);
    this.loadMatches();
  }

  onDateChange(event: Event): void {
    this.setDate((event.target as HTMLInputElement).value);
  }

  private setDate(iso: string): void {
    if (!iso) return;
    this.selectedDate.set(iso);
    this.isToday.set(iso === todayIso());
    this.page.set(0);
    this.loadMatches();
  }

  onCompetitionChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.competitionId.set(value || null);
    this.seasonId.set(null);
    if (this.competitionId()) {
      this.loadSeasons(this.competitionId()!);
    } else {
      this.seasons.set([]);
    }
    this.page.set(0);
    this.loadMatches();
  }

  onSeasonChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.seasonId.set(value || null);
    this.page.set(0);
    this.loadMatches();
  }

  changePage(p: number): void {
    this.page.set(p);
    this.loadMatches();
  }

  openTeam(t: TeamRef): void {
    this.router.navigate(['/team', t.id]);
  }

  private loadSeasons(competitionId: string): void {
    this.api.seasons(competitionId).subscribe({
      next: (seasons) => {
        this.seasons.set(seasons);
        const current = seasons.find((s) => s.current) ?? seasons[0];
        if (current && !this.seasonId()) {
          this.seasonId.set(current.id);
          this.loadMatches();
        }
      },
    });
  }

  loadTeams(): void {
    this.teamsLoading.set(true);
    this.teamsError.set(null);
    this.api.teams().subscribe({
      next: (rows) => {
        this.teams.set(rows);
        this.teamsLoading.set(false);
      },
      error: () => {
        this.teams.set([]);
        this.teamsLoading.set(false);
        this.teamsError.set('Impossible de charger les équipes.');
      },
    });
  }

  loadNews(): void {
    this.newsLoading.set(true);
    this.newsError.set(null);
    this.api.news(this.competitionId() ?? undefined).subscribe({
      next: (rows) => {
        this.news.set(rows);
        this.newsLoading.set(false);
      },
      error: () => {
        this.news.set([]);
        this.newsLoading.set(false);
        this.newsError.set('Impossible de charger les actualités.');
      },
    });
  }

  loadMatches(): void {
    this.loading.set(true);
    this.error.set(null);
    this.api
      .matches({
        date: this.liveOnly() ? undefined : this.selectedDate(),
        seasonId: this.seasonId() ?? undefined,
        competitionId: this.competitionId() ?? undefined,
        live: this.liveOnly() || undefined,
        page: this.page(),
        size: 60,
      })
      .subscribe({
        next: (pageInfo) => {
          this.pageInfo.set(pageInfo);
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
          this.pageInfo.set(null);
          this.error.set('Impossible de charger les matchs. Vérifiez votre clé API dans les paramètres.');
        },
      });
  }

  fmtNewsDate(value: string): string {
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? '' : d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' });
  }
}
