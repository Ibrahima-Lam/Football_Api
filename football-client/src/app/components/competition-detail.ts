import { Component, Input, OnInit, inject, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../services/api.service';
import { LiveService } from '../services/live.service';
import { MatchListComponent } from './match-list';
import { CompetitionDetail, CompetitionRef, LiveStandingMessage, MatchCard, NewsItem, PageInfo, PlayerSeasonStatItem, RefereeItem, SeasonRef, StandingItem, TeamRef, CoachItem } from '../models/models';
import { addDays, isoDate, todayIso } from '../utils';

@Component({
  selector: 'app-competition-detail',
  standalone: true,
  imports: [MatchListComponent, RouterLink],
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
    } @else if (competition(); as c) {
      <section class="fc-hero p-4 mb-4">
        <a routerLink="/competitions" class="text-white-50 text-decoration-none small fw-semibold d-inline-block mb-3">
          <i class="bi bi-arrow-left me-1"></i>Compétitions
        </a>
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-3">
          @if (c.logo) {
            <img class="fc-crest fc-crest-lg" [src]="c.logo" [alt]="c.name" />
          } @else {
            <div class="fc-crest fc-crest-lg d-flex align-items-center justify-content-center text-white bg-white bg-opacity-10">
              <i class="bi bi-trophy fs-3"></i>
            </div>
          }
          <div class="flex-grow-1">
            <h1 class="h4 fw-bold mb-1">{{ c.name }}</h1>
            <div class="small text-white-50">
              <span>{{ c.countryName || c.confederationName || 'International' }}</span>
              @if (c.type) {
                <span class="mx-1">·</span><span>{{ c.type }}</span>
              }
              @if (c.gender) {
                <span class="mx-1">·</span><span>{{ c.gender }}</span>
              }
            </div>
          </div>
          @if (seasons().length > 0) {
            <div>
              <select class="form-select fc-select" style="min-width: 220px" [value]="selectedSeasonId() ?? ''"
                (change)="onSeasonChange($event)">
                @for (s of seasons(); track s.id) {
                  <option [value]="s.id">{{ s.name }} @if (s.current) { (actuelle) }</option>
                }
              </select>
            </div>
          }
        </div>
      </section>

      <div class="fc-tabs nav nav-pills gap-2 mb-3">
        <a class="nav-link" [class.active]="tab() === 'standings'" (click)="setTab('standings')">
          <i class="bi bi-list-ol me-1"></i>Classement
        </a>
        <a class="nav-link" [class.active]="tab() === 'matches'" (click)="setTab('matches')">
          <i class="bi bi-calendar-week me-1"></i>Matchs
        </a>
        <a class="nav-link" [class.active]="tab() === 'teams'" (click)="setTab('teams')">
          <i class="bi bi-shield me-1"></i>Équipes
        </a>
        <a class="nav-link" [class.active]="tab() === 'stats'" (click)="setTab('stats')">
          <i class="bi bi-bar-chart me-1"></i>Statistiques
        </a>
        <a class="nav-link" [class.active]="tab() === 'referees'" (click)="setTab('referees')">
          <i class="bi bi-whistle me-1"></i>Arbitres
        </a>
        <a class="nav-link" [class.active]="tab() === 'news'" (click)="setTab('news')">
          <i class="bi bi-newspaper me-1"></i>News
        </a>
        <a class="nav-link" [class.active]="tab() === 'coaches'" (click)="setTab('coaches')">
          <i class="bi bi-person-badge me-1"></i>Coachs
        </a>
      </div>

      @if (tab() === 'standings') {
        @if (standingsLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement du classement…
          </div>
        } @else if (standings().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-list-ol display-6 d-block mb-2"></i>
            Aucun classement disponible pour cette saison.
          </div>
        } @else {
          @for (group of standingsGroups(); track group.groupId) {
            <div class="fc-panel overflow-auto mb-3">
              <div class="d-flex align-items-center gap-2 px-3 pt-3 pb-2">
                <i class="bi bi-people-fill text-primary"></i>
                <h6 class="mb-0 fw-bold">{{ group.groupName }}</h6>
              </div>
              <table class="table fc-table mb-0 align-middle">
                <thead>
                  <tr>
                    <th class="ps-3">#</th>
                    <th>Équipe</th>
                    <th class="text-center">J</th>
                    <th class="text-center">G</th>
                    <th class="text-center">N</th>
                    <th class="text-center">P</th>
                    <th class="text-center">BP</th>
                    <th class="text-center">BC</th>
                    <th class="text-center">Diff</th>
                    <th class="text-center">Pts</th>
                    <th class="text-center pe-3">Forme</th>
                  </tr>
                </thead>
                <tbody>
                  @for (row of group.rows; track row.id) {
                    <tr>
                      <td class="ps-3">
                        <span class="d-inline-block me-2 rounded" style="width:4px;height:20px"
                          [class]="rankZone(row.rankPosition, group.rows.length)"></span>
                        {{ row.rankPosition }}
                      </td>
                      <td>
                        <div class="d-flex align-items-center gap-2">
                          @if (row.team.logo) {
                            <img class="fc-crest-sm" [src]="row.team.logo" [alt]="row.team.name" loading="lazy" />
                          }
                          <span class="fw-semibold text-truncate" style="max-width: 220px">{{ row.team.name }}</span>
                        </div>
                      </td>
                      <td class="text-center">{{ row.played }}</td>
                      <td class="text-center">{{ row.wins }}</td>
                      <td class="text-center">{{ row.draws }}</td>
                      <td class="text-center">{{ row.losses }}</td>
                      <td class="text-center">{{ row.goalsFor }}</td>
                      <td class="text-center">{{ row.goalsAgainst }}</td>
                      <td class="text-center fw-bold" [class.text-success]="row.goalDifference > 0"
                        [class.text-danger]="row.goalDifference < 0">
                        {{ row.goalDifference > 0 ? '+' : '' }}{{ row.goalDifference }}
                      </td>
                      <td class="text-center fw-bold">{{ row.points }}</td>
                      <td class="text-center pe-3">
                        @for (f of formChars(row.form); track $index) {
                          <span class="d-inline-flex align-items-center justify-content-center rounded-1 text-white fw-bold mx-1"
                            style="width: 18px; height: 18px; font-size: 10px" [style.background]="f.color">
                            {{ f.char }}
                          </span>
                        }
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          }
        }
      }

      @if (tab() === 'matches') {
        <div class="d-flex flex-wrap align-items-center gap-2 mb-3">
          <button class="fc-date-btn" (click)="prevMatchDay()"><i class="bi bi-chevron-left"></i></button>
          <span class="fw-bold">{{ matchDate() }}</span>
          <button class="fc-date-btn" (click)="nextMatchDay()"><i class="bi bi-chevron-right"></i></button>
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

      @if (tab() === 'teams') {
        @if (teamsLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des équipes…
          </div>
        } @else if (teams().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-shield display-6 d-block mb-2"></i>
            Aucune équipe inscrite pour cette saison.
          </div>
        } @else {
          <div class="row g-3">
            @for (t of teams(); track t.id) {
              <div class="col-6 col-md-4 col-lg-3">
                <div class="fc-panel d-flex align-items-center gap-3 p-3 h-100">
                  @if (t.logo) {
                    <img class="fc-crest" [src]="t.logo" [alt]="t.name" loading="lazy" />
                  } @else {
                    <div class="fc-crest d-flex align-items-center justify-content-center text-muted bg-body-tertiary">
                      <i class="bi bi-shield"></i>
                    </div>
                  }
                  <div>
                    <div class="fw-bold">{{ t.name }}</div>
                    <div class="small text-muted">
                      @if (t.countryFlag) {
                        <img [src]="t.countryFlag" [alt]="t.countryIso2 ?? ''" style="width: 18px; height: 12px; object-fit: cover; border-radius: 2px" loading="lazy" />
                      }
                      {{ t.countryIso2 ?? '' }}
                    </div>
                  </div>
                </div>
              </div>
            }
          </div>
        }
      }

      @if (tab() === 'stats') {
        <div class="fc-tabs nav nav-pills gap-2 mb-3">
          <a class="nav-link" [class.active]="statTab() === 'scorers'" (click)="setStatTab('scorers')">
            <i class="bi bi-graph-up-arrow me-1"></i>Buteurs
          </a>
          <a class="nav-link" [class.active]="statTab() === 'assists'" (click)="setStatTab('assists')">
            <i class="bi bi-person-lines-fill me-1"></i>Passeurs
          </a>
          <a class="nav-link" [class.active]="statTab() === 'cards'" (click)="setStatTab('cards')">
            <i class="bi bi-square-fill text-warning me-1"></i>Cartons
          </a>
        </div>

        @if (playerStatsLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des statistiques…
          </div>
        } @else if (playerStats().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-bar-chart display-6 d-block mb-2"></i>
            Aucune statistique disponible pour cette saison.
          </div>
        } @else {
          <div class="fc-panel overflow-auto">
            <table class="table fc-table mb-0 align-middle">
              <thead>
                <tr>
                  <th class="ps-3">#</th>
                  <th>Joueur</th>
                  <th>Équipe</th>
                  <th class="text-center">Matchs</th>
                  @if (statTab() === 'scorers') {
                    <th class="text-center">Buts</th>
                  } @else if (statTab() === 'assists') {
                    <th class="text-center">Passes</th>
                  } @else {
                    <th class="text-center">Jaunes</th>
                    <th class="text-center">Rouges</th>
                  }
                  <th class="text-center">Note</th>
                </tr>
              </thead>
              <tbody>
                @for (p of playerStats(); track p.player.id; let i = $index) {
                  <tr>
                    <td class="ps-3 fw-bold text-muted">{{ i + 1 }}</td>
                    <td>
                      <div class="d-flex align-items-center gap-2">
                        @if (p.player.photo) {
                          <img class="rounded-circle" [src]="p.player.photo" [alt]="p.player.fullName" style="width: 26px; height: 26px; object-fit: cover" loading="lazy" />
                        }
                        <span class="fw-semibold">{{ p.player.fullName }}</span>
                      </div>
                    </td>
                    <td class="text-muted small">{{ p.team.name }}</td>
                    <td class="text-center">{{ p.appearances }}</td>
                    @if (statTab() === 'scorers') {
                      <td class="text-center fw-bold">{{ p.goals }}</td>
                    } @else if (statTab() === 'assists') {
                      <td class="text-center fw-bold">{{ p.assists }}</td>
                    } @else {
                      <td class="text-center">
                        <span class="d-inline-block rounded" style="width: 12px; height: 18px; background: #f59e0b"></span>
                        {{ p.yellowCards }}
                      </td>
                      <td class="text-center">
                        <span class="d-inline-block rounded" style="width: 12px; height: 18px; background: #ef4444"></span>
                        {{ p.redCards }}
                      </td>
                    }
                    <td class="text-center fw-bold">{{ p.avgRating !== null ? p.avgRating : '—' }}</td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        }
      }

      @if (tab() === 'referees') {
        @if (refereesLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des arbitres…
          </div>
        } @else if (referees().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-whistle display-6 d-block mb-2"></i>
            Aucun arbitre référencé pour cette saison.
          </div>
        } @else {
          <div class="row g-3">
            @for (r of referees(); track r.id) {
              <div class="col-md-6 col-lg-4">
                <div class="fc-panel d-flex align-items-center gap-3 p-3 h-100">
                  @if (r.photo) {
                    <img class="rounded-circle" [src]="r.photo" [alt]="r.fullName" style="width: 44px; height: 44px; object-fit: cover" loading="lazy" />
                  } @else {
                    <div class="rounded-circle d-flex align-items-center justify-content-center text-muted bg-body-tertiary" style="width: 44px; height: 44px">
                      <i class="bi bi-person"></i>
                    </div>
                  }
                  <div class="flex-grow-1">
                    <div class="fw-bold">{{ r.fullName }}</div>
                    <div class="small text-muted">
                      @if (r.countryFlag) {
                        <img [src]="r.countryFlag" [alt]="r.countryName ?? ''" style="width: 18px; height: 12px; object-fit: cover; border-radius: 2px" class="me-1" loading="lazy" />
                      }
                      {{ r.category }}
                      @if (r.roles.length > 0) {
                        · {{ r.roles.join(', ') }}
                      }
                    </div>
                  </div>
                  <span class="fc-badge fc-badge-muted">{{ r.matchesCount }} matchs</span>
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
            Aucune actualité pour cette compétition.
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

      @if (tab() === 'coaches') {
        @if (coachesLoading()) {
          <div class="fc-loading">
            <div class="spinner-border text-primary me-2" role="status"></div>
            Chargement des coachs…
          </div>
        } @else if (coaches().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-person-badge display-6 d-block mb-2"></i>
            Aucun coach référencé pour cette saison.
          </div>
        } @else {
          <div class="row g-3">
            @for (c of coaches(); track c.id) {
              <div class="col-md-6 col-lg-4">
                <div class="fc-panel d-flex align-items-center gap-3 p-3 h-100">
                  @if (c.photo) {
                    <img class="rounded-circle" [src]="c.photo" [alt]="c.fullName" style="width: 44px; height: 44px; object-fit: cover" loading="lazy" />
                  } @else {
                    <div class="rounded-circle d-flex align-items-center justify-content-center text-muted bg-body-tertiary" style="width: 44px; height: 44px">
                      <i class="bi bi-person"></i>
                    </div>
                  }
                  <div class="flex-grow-1">
                    <div class="fw-bold">
                      {{ c.fullName }}
                      @if (c.interim) {
                        <span class="fc-badge fc-badge-muted ms-1">intérim</span>
                      }
                    </div>
                    <div class="small text-muted">
                      @if (c.team.logo) {
                        <img class="fc-crest-sm me-1" [src]="c.team.logo" [alt]="c.team.name" loading="lazy" />
                      }
                      {{ c.team.name }} · {{ c.role }}
                    </div>
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
export class CompetitionDetailComponent implements OnInit {
  @Input() id!: string;

  private api = inject(ApiService);
  private live = inject(LiveService);

  competition = signal<CompetitionRef | null>(null);
  seasons = signal<SeasonRef[]>([]);
  selectedSeasonId = signal<string | null>(null);
  standings = signal<StandingItem[]>([]);
  standingsGroups = computed(() => {
    const groups = new Map<string, { groupId: string | null; groupName: string; rows: StandingItem[] }>();
    for (const row of this.standings()) {
      const key = row.groupId ?? '__ungrouped__';
      if (!groups.has(key)) {
        groups.set(key, { groupId: row.groupId, groupName: row.groupName || 'Classement', rows: [] });
      }
      groups.get(key)!.rows.push(row);
    }
    return [...groups.values()];
  });
  matches = signal<MatchCard[]>([]);
  teams = signal<TeamRef[]>([]);
  playerStats = signal<PlayerSeasonStatItem[]>([]);
  referees = signal<RefereeItem[]>([]);
  news = signal<NewsItem[]>([]);
  coaches = signal<CoachItem[]>([]);
  matchDate = signal(todayIso());
  tab = signal<'standings' | 'matches' | 'teams' | 'stats' | 'referees' | 'news' | 'coaches'>('standings');
  statTab = signal<'scorers' | 'assists' | 'cards'>('scorers');
  loading = signal(true);
  standingsLoading = signal(true);
  matchesLoading = signal(true);
  teamsLoading = signal(true);
  playerStatsLoading = signal(true);
  refereesLoading = signal(true);
  newsLoading = signal(true);
  coachesLoading = signal(true);
  error = signal<string | null>(null);

  todayIso = todayIso;

  constructor() {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.api.competitionDetail(this.id).subscribe({
      next: (detail: CompetitionDetail) => {
        this.competition.set(detail.competition);
        this.seasons.set(detail.seasons);
        const selected = detail.seasons.find((s) => s.current) ?? detail.seasons[0];
        if (selected) {
          this.selectedSeasonId.set(selected.id);
        }
        this.loading.set(false);
        this.loadStandings();
        this.loadMatches();
        this.loadNews();
        this.listenStandings();
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Impossible de charger cette compétition.');
      },
    });
  }

  setTab(tab: 'standings' | 'matches' | 'teams' | 'stats' | 'referees' | 'news' | 'coaches'): void {
    this.tab.set(tab);
    if (tab === 'matches') {
      this.loadMatches();
    } else if (tab === 'teams') {
      this.loadTeams();
    } else if (tab === 'stats') {
      this.loadPlayerStats(this.statTab());
    } else if (tab === 'referees') {
      this.loadReferees();
    } else if (tab === 'news') {
      this.loadNews();
    } else if (tab === 'coaches') {
      this.loadCoaches();
    }
  }

  setStatTab(stat: 'scorers' | 'assists' | 'cards'): void {
    this.statTab.set(stat);
    this.loadPlayerStats(stat);
  }

  onSeasonChange(event: Event): void {
    this.selectedSeasonId.set((event.target as HTMLSelectElement).value || null);
    this.loadStandings();
    this.loadMatches();
    this.loadTeams();
    this.loadPlayerStats(this.statTab());
    this.loadReferees();
    this.loadCoaches();
  }

  private loadStandings(): void {
    if (!this.selectedSeasonId()) {
      this.standings.set([]);
      this.standingsLoading.set(false);
      return;
    }
    this.standingsLoading.set(true);
    this.api.standings(this.selectedSeasonId()!).subscribe({
      next: (rows) => {
        this.standings.set(rows);
        this.standingsLoading.set(false);
      },
      error: () => {
        this.standings.set([]);
        this.standingsLoading.set(false);
      },
    });
  }

  private listenStandings(): void {
    this.live.subscribe<LiveStandingMessage>('/topic/standings').subscribe((msg) => {
      if (!this.selectedSeasonId() || msg.seasonId !== this.selectedSeasonId()) return;
      const idx = this.standings().findIndex((s) => s.team.id === msg.teamId);
      if (idx === -1) return;
      this.standings.update((rows) =>
        rows.map((s) =>
          s.team.id === msg.teamId
            ? { ...s, played: msg.played, wins: msg.wins, draws: msg.draws, losses: msg.losses, goalsFor: msg.goalsFor, goalsAgainst: msg.goalsAgainst, goalDifference: msg.goalDifference, points: msg.points, form: msg.form }
            : s,
        ),
      );
    });
  }

  prevMatchDay(): void {
    this.setMatchDate(isoDate(addDays(new Date(this.matchDate() + 'T12:00:00'), -1)));
  }

  nextMatchDay(): void {
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
      .matches({
        date: this.matchDate(),
        competitionId: this.competition()?.id ?? this.id,
        seasonId: this.selectedSeasonId() ?? undefined,
        size: 100,
      })
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

  private loadTeams(): void {
    const seasonId = this.selectedSeasonId();
    if (!seasonId) {
      this.teams.set([]);
      this.teamsLoading.set(false);
      return;
    }
    this.teamsLoading.set(true);
    this.api.teams(seasonId).subscribe({
      next: (rows) => {
        this.teams.set(rows);
        this.teamsLoading.set(false);
      },
      error: () => {
        this.teams.set([]);
        this.teamsLoading.set(false);
      },
    });
  }

  private loadPlayerStats(stat: 'scorers' | 'assists' | 'cards'): void {
    const seasonId = this.selectedSeasonId();
    if (!seasonId) {
      this.playerStats.set([]);
      this.playerStatsLoading.set(false);
      return;
    }
    this.playerStatsLoading.set(true);
    this.api.playerSeasonStats(seasonId, stat).subscribe({
      next: (rows) => {
        this.playerStats.set(rows);
        this.playerStatsLoading.set(false);
      },
      error: () => {
        this.playerStats.set([]);
        this.playerStatsLoading.set(false);
      },
    });
  }

  private loadReferees(): void {
    const seasonId = this.selectedSeasonId();
    if (!seasonId) {
      this.referees.set([]);
      this.refereesLoading.set(false);
      return;
    }
    this.refereesLoading.set(true);
    this.api.seasonReferees(seasonId).subscribe({
      next: (rows) => {
        this.referees.set(rows);
        this.refereesLoading.set(false);
      },
      error: () => {
        this.referees.set([]);
        this.refereesLoading.set(false);
      },
    });
  }

  private loadNews(): void {
    this.newsLoading.set(true);
    this.api.news(this.competition()?.id ?? this.id).subscribe({
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

  private loadCoaches(): void {
    const seasonId = this.selectedSeasonId();
    if (!seasonId) {
      this.coaches.set([]);
      this.coachesLoading.set(false);
      return;
    }
    this.coachesLoading.set(true);
    this.api.seasonCoaches(seasonId).subscribe({
      next: (rows) => {
        this.coaches.set(rows);
        this.coachesLoading.set(false);
      },
      error: () => {
        this.coaches.set([]);
        this.coachesLoading.set(false);
      },
    });
  }

  fmtNewsDate(value: string): string {
    const d = new Date(value);
    return Number.isNaN(d.getTime()) ? '' : d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'short', year: 'numeric' });
  }

  rankZone(rank: number, groupSize: number): string {
    if (rank <= 4) return 'fc-promo';
    if (rank > groupSize - 3) return 'fc-releg';
    return 'bg-body-tertiary';
  }

  formChars(form: string | null): { char: string; color: string }[] {
    if (!form) return [];
    return [...form].map((char) => ({
      char: char.toUpperCase(),
      color: char.toUpperCase() === 'W' ? '#10b981' : char.toUpperCase() === 'L' ? '#ef4444' : '#94a3b8',
    }));
  }
}
