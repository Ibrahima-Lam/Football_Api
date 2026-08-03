import { Component, Input, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../services/api.service';
import { LiveService } from '../services/live.service';
import {
  LiveEventMessage,
  LiveScoreMessage,
  LineupItem,
  MatchDetail,
  MatchEventItem,
  PlayerStatItem,
  TeamRef,
  TeamStatItem,
} from '../models/models';
import { fmtTime, isLive, isFinished, scoreSub, statusLabel } from '../utils';

@Component({
  selector: 'app-match-detail',
  standalone: true,
  imports: [RouterLink],
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
        Chargement du match…
      </div>
    } @else if (detail(); as d) {
      <a routerLink="/" class="text-muted text-decoration-none small fw-semibold d-inline-block mb-3">
        <i class="bi bi-arrow-left me-1"></i>Retour aux matchs
      </a>

      <div class="fc-scoreboard p-4 mb-3">
        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
          <span class="fc-badge" [class.fc-badge-live]="isLive(d.match)" [class.fc-badge-finished]="isFinished(d.match)">
            {{ d.match.competition.name }}
          </span>
          <span class="small text-white-50">
            {{ d.match.stageName }}{{ d.match.roundName ? ' · ' + d.match.roundName : '' }}
          </span>
          <span class="fc-badge" [class.fc-badge-live]="isLive(d.match)"
            [class.fc-badge-finished]="isFinished(d.match)" [class.fc-badge-scheduled]="!isLive(d.match) && !isFinished(d.match)">
            @if (isLive(d.match)) {
              <span class="fc-live-dot me-1"></span>{{ d.match.minute ?? '' }}'
            } @else {
              {{ statusLabel(d.match.status) }}
            }
          </span>
        </div>

        <div class="d-flex align-items-center justify-content-between gap-2 flex-wrap">
          <div class="d-flex flex-column align-items-center gap-2 text-center" style="flex: 1">
            @if (d.match.homeTeam.logo) {
              <img class="fc-crest fc-crest-lg" [src]="d.match.homeTeam.logo" [alt]="d.match.homeTeam.name" />
            }
            <div class="fw-bold fs-5 px-2">{{ d.match.homeTeam.name }}</div>
          </div>

          <div class="text-center" style="min-width: 180px">
            <div class="num mb-1">{{ score() }}</div>
            @if (isLive(d.match)) {
              <span class="fc-badge fc-badge-live mb-2">{{ d.match.minute ?? '' }}'</span>
            } @else {
              <div class="small text-white-50 mb-1">{{ fmtTime(d.match.kickoff) }}</div>
            }
            @if (scoreSub(d.match)) {
              <div class="small text-white-50">{{ scoreSub(d.match) }}</div>
            }
            @if (d.homePenaltyForm || d.awayPenaltyForm) {
              <div class="small text-white-50">
                Tirs au but : {{ d.match.homeTeam.shortName ?? d.match.homeTeam.name }} {{ d.homePenaltyForm }}
                — {{ d.awayPenaltyForm }} {{ d.match.awayTeam.shortName ?? d.match.awayTeam.name }}
              </div>
            }
          </div>

          <div class="d-flex flex-column align-items-center gap-2 text-center" style="flex: 1">
            @if (d.match.awayTeam.logo) {
              <img class="fc-crest fc-crest-lg" [src]="d.match.awayTeam.logo" [alt]="d.match.awayTeam.name" />
            }
            <div class="fw-bold fs-5 px-2">{{ d.match.awayTeam.name }}</div>
          </div>
        </div>
      </div>

      <div class="d-flex flex-wrap gap-2 mb-4">
        @if (d.match.stadiumName) {
          <span class="fc-badge fc-badge-muted"><i class="bi bi-geo-alt me-1"></i>{{ d.match.stadiumName }}{{ d.stadiumCity ? ' · ' + d.stadiumCity : '' }}</span>
        }
        @if (d.refereeName) {
          <span class="fc-badge fc-badge-muted"><i class="bi bi-person-video3 me-1"></i>{{ d.refereeName }}</span>
        }
        @if (d.attendance !== null) {
          <span class="fc-badge fc-badge-muted"><i class="bi bi-people me-1"></i>{{ attendance() }}</span>
        }
        @if (d.weather) {
          <span class="fc-badge fc-badge-muted"><i class="bi bi-cloud-sun me-1"></i>{{ d.weather }}{{ d.temperature !== null ? ' · ' + d.temperature + '°C' : '' }}</span>
        }
      </div>

      <div class="fc-tabs nav nav-pills gap-2 mb-3">
        <a class="nav-link" [class.active]="tab() === 'resume'" (click)="setTab('resume')">
          <i class="bi bi-list-check me-1"></i>Résumé
        </a>
        <a class="nav-link" [class.active]="tab() === 'stats'" (click)="setTab('stats')">
          <i class="bi bi-bar-chart me-1"></i>Statistiques
        </a>
        <a class="nav-link" [class.active]="tab() === 'lineups'" (click)="setTab('lineups')">
          <i class="bi bi-people me-1"></i>Compositions
        </a>
      </div>

      @if (tab() === 'resume') {
        @if (events().length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-list-check display-6 d-block mb-2"></i>
            Aucun événement pour le moment.
          </div>
        } @else {
          <div class="fc-panel p-4">
            <div class="fc-timeline">
              @for (e of events(); track e.id) {
                <div class="fc-tl-item" [class.fc-tl-goal]="isGoal(e.eventType)"
                  [class.fc-tl-card]="isCard(e.eventType)" [class.fc-tl-sub]="isSub(e.eventType)">
                  <div class="fc-tl-dot"><i [class]="eventIcon(e.eventType)"></i></div>
                  <div class="d-flex align-items-start gap-2">
                    <span class="fw-bold text-muted" style="min-width: 44px">{{ e.minute }}{{ e.extraMinute ? '+' + e.extraMinute : '' }}'</span>
                    <div>
                      <div class="fw-semibold">{{ eventText(e) }}</div>
                      <div class="small text-muted">
                        @if (e.team) {
                          <span>{{ e.team.name }}</span>
                        }
                        @if (e.varReviewed) {
                          <span class="ms-2 fc-badge fc-badge-muted"><i class="bi bi-tv me-1"></i>VAR</span>
                        }
                      </div>
                    </div>
                  </div>
                </div>
              }
            </div>
          </div>
        }
      }

      @if (tab() === 'stats') {
        @if (d.teamStats.length < 2) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-bar-chart display-6 d-block mb-2"></i>
            Statistiques indisponibles pour ce match.
          </div>
        } @else {
          <div class="fc-panel p-4 mb-4">
            <div class="fc-section-title mb-3">Possession & attaque</div>
            @for (s of featuredStats(); track s.key) {
              <div class="fc-stat-row">
                <div class="text-end fw-bold" style="min-width: 3ch">{{ s.home }}</div>
                <div class="fc-bar home flex-grow-1"><span [style.width.%]="s.homePct"></span></div>
                <div class="text-center text-muted small fw-semibold" style="min-width: 110px">{{ s.label }}</div>
                <div class="fc-bar flex-grow-1"><span [style.width.%]="s.awayPct"></span></div>
                <div class="fw-bold" style="min-width: 3ch">{{ s.away }}</div>
              </div>
            }
          </div>

          <div class="row g-3">
            <div class="col-md-6">
              <div class="fc-panel overflow-auto">
                <div class="p-3 border-bottom fw-bold d-flex align-items-center gap-2">
                  @if (homeStat()?.team?.logo) {
                    <img class="fc-crest-sm" [src]="homeStat()?.team?.logo" [alt]="homeStat()?.team?.name" />
                  }
                  {{ homeStat()?.team?.name }}
                </div>
                <table class="table fc-table mb-0 align-middle">
                  <thead>
                    <tr>
                      <th class="ps-3">Joueur</th>
                      <th class="text-center">Buts</th>
                      <th class="text-center">Passes</th>
                      <th class="text-center">Tirs</th>
                      <th class="text-center">Tirs cadrés</th>
                      <th class="text-center">Note</th>
                    </tr>
                  </thead>
                  <tbody>
                    @for (p of homePlayers(); track p.player.id) {
                      <tr>
                        <td class="ps-3">
                          <span class="fw-semibold">{{ p.player.fullName }}</span>
                          <span class="text-muted small"> · {{ p.minutesPlayed }}'</span>
                        </td>
                        <td class="text-center fw-bold">{{ p.goals }}</td>
                        <td class="text-center">{{ p.assists }}</td>
                        <td class="text-center">{{ p.shots }}</td>
                        <td class="text-center">{{ p.shotsOnTarget }}</td>
                        <td class="text-center fw-bold">{{ p.rating !== null ? p.rating : '—' }}</td>
                      </tr>
                    }
                  </tbody>
                </table>
              </div>
            </div>

            <div class="col-md-6">
              <div class="fc-panel overflow-auto">
                <div class="p-3 border-bottom fw-bold d-flex align-items-center gap-2">
                  @if (awayStat()?.team?.logo) {
                    <img class="fc-crest-sm" [src]="awayStat()?.team?.logo" [alt]="awayStat()?.team?.name" />
                  }
                  {{ awayStat()?.team?.name }}
                </div>
                <table class="table fc-table mb-0 align-middle">
                  <thead>
                    <tr>
                      <th class="ps-3">Joueur</th>
                      <th class="text-center">Buts</th>
                      <th class="text-center">Passes</th>
                      <th class="text-center">Tirs</th>
                      <th class="text-center">Tirs cadrés</th>
                      <th class="text-center">Note</th>
                    </tr>
                  </thead>
                  <tbody>
                    @for (p of awayPlayers(); track p.player.id) {
                      <tr>
                        <td class="ps-3">
                          <span class="fw-semibold">{{ p.player.fullName }}</span>
                          <span class="text-muted small"> · {{ p.minutesPlayed }}'</span>
                        </td>
                        <td class="text-center fw-bold">{{ p.goals }}</td>
                        <td class="text-center">{{ p.assists }}</td>
                        <td class="text-center">{{ p.shots }}</td>
                        <td class="text-center">{{ p.shotsOnTarget }}</td>
                        <td class="text-center fw-bold">{{ p.rating !== null ? p.rating : '—' }}</td>
                      </tr>
                    }
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        }
      }

      @if (tab() === 'lineups') {
        @if (d.lineups.length === 0) {
          <div class="fc-panel text-center py-5 text-muted">
            <i class="bi bi-people display-6 d-block mb-2"></i>
            Compositions indisponibles pour ce match.
          </div>
        } @else {
          <div class="row g-3">
            <div class="col-md-6">
              <div class="fc-panel p-3">
                <div class="fw-bold mb-3 d-flex align-items-center gap-2">
                  @if (d.match.homeTeam.logo) {
                    <img class="fc-crest-sm" [src]="d.match.homeTeam.logo" [alt]="d.match.homeTeam.name" />
                  }
                  {{ d.match.homeTeam.name }}
                </div>
                @for (l of homeLineup(); track l.player.id) {
                  <div class="d-flex align-items-center gap-2 py-1">
                    <span class="d-inline-flex align-items-center justify-content-center fw-bold rounded-2 text-muted"
                      style="width: 26px; height: 26px; background: #f1f5f9">{{ l.shirtNumber ?? '—' }}</span>
                    <span class="fw-semibold">{{ l.player.fullName }}</span>
                    @if (l.captain) {
                      <span class="fc-badge fc-badge-muted" title="Capitaine">C</span>
                    }
                    @if (l.position) {
                      <span class="small text-muted ms-auto">{{ l.position }}</span>
                    }
                  </div>
                }
              </div>
            </div>
            <div class="col-md-6">
              <div class="fc-panel p-3">
                <div class="fw-bold mb-3 d-flex align-items-center gap-2">
                  @if (d.match.awayTeam.logo) {
                    <img class="fc-crest-sm" [src]="d.match.awayTeam.logo" [alt]="d.match.awayTeam.name" />
                  }
                  {{ d.match.awayTeam.name }}
                </div>
                @for (l of awayLineup(); track l.player.id) {
                  <div class="d-flex align-items-center gap-2 py-1">
                    <span class="d-inline-flex align-items-center justify-content-center fw-bold rounded-2 text-muted"
                      style="width: 26px; height: 26px; background: #f1f5f9">{{ l.shirtNumber ?? '—' }}</span>
                    <span class="fw-semibold">{{ l.player.fullName }}</span>
                    @if (l.captain) {
                      <span class="fc-badge fc-badge-muted" title="Capitaine">C</span>
                    }
                    @if (l.position) {
                      <span class="small text-muted ms-auto">{{ l.position }}</span>
                    }
                  </div>
                }
              </div>
            </div>
          </div>
        }
      }
    }
  `,
})
export class MatchDetailComponent implements OnDestroy, OnInit {
  @Input() id!: string;

  private api = inject(ApiService);
  private live = inject(LiveService);

  detail = signal<MatchDetail | null>(null);
  tab = signal<'resume' | 'stats' | 'lineups'>('resume');
  loading = signal(true);
  error = signal<string | null>(null);
  private liveEvents = signal<LiveEventMessage[]>([]);
  private subscriptions: { unsubscribe(): void }[] = [];

  isLive = isLive;
  isFinished = isFinished;
  statusLabel = statusLabel;
  fmtTime = fmtTime;
  scoreSub = scoreSub;

  readonly events = computed<MatchEventItem[]>(() => {
    const base = [...(this.detail()?.events ?? [])];
    for (const le of this.liveEvents()) {
      if (!base.some((e) => e.id === le.id)) {
        base.push({
          id: le.id,
          minute: le.minute,
          extraMinute: le.extraMinute,
          period: le.period,
          team: this.teamRef(le.teamId),
          player: null,
          relatedPlayer: null,
          eventType: le.eventType,
          detail: le.detail,
          comments: le.comments,
          varReviewed: le.varReviewed,
        });
      }
    }
    base.sort((a, b) => a.minute - b.minute || (a.extraMinute ?? 0) - (b.extraMinute ?? 0));
    return base;
  });

  readonly attendance = computed<string>(() => (this.detail()?.attendance ?? 0).toLocaleString('fr-FR'));

  readonly homeStat = computed<TeamStatItem | undefined>(() => {
    const detail = this.detail();
    const stats = detail?.teamStats ?? [];
    return stats.find((s) => s.team.id === detail?.match.homeTeam.id) ?? stats[0];
  });

  readonly awayStat = computed<TeamStatItem | undefined>(() => {
    const detail = this.detail();
    const stats = detail?.teamStats ?? [];
    return stats.find((s) => s.team.id === detail?.match.awayTeam.id) ?? stats[1];
  });

  readonly homePlayers = computed<PlayerStatItem[]>(() =>
    (this.detail()?.playerStats ?? []).filter((p) => p.team.id === this.detail()?.match.homeTeam.id),
  );

  readonly awayPlayers = computed<PlayerStatItem[]>(() =>
    (this.detail()?.playerStats ?? []).filter((p) => p.team.id === this.detail()?.match.awayTeam.id),
  );

  readonly homeLineup = computed<LineupItem[]>(() =>
    (this.detail()?.lineups ?? []).filter((l) => l.team.id === this.detail()?.match.homeTeam.id),
  );

  readonly awayLineup = computed<LineupItem[]>(() =>
    (this.detail()?.lineups ?? []).filter((l) => l.team.id === this.detail()?.match.awayTeam.id),
  );

  readonly featuredStats = computed(() => {
    const home = this.homeStat();
    const away = this.awayStat();
    const pct = (h: number, a: number): [number, number] => {
      const total = h + a;
      if (total === 0) return [0, 0];
      return [Math.round((h / total) * 100), Math.round((a / total) * 100)];
    };
    const poss = (h: number | null | undefined, a: number | null | undefined): [number, number] => {
      const total = (h ?? 0) + (a ?? 0);
      if (total === 0) return [0, 0];
      return [Math.round(((h ?? 0) / total) * 100), Math.round(((a ?? 0) / total) * 100)];
    };
    const [hp, ap] = poss(home?.possession, away?.possession);
    const [sh, sa] = pct(home?.shots ?? 0, away?.shots ?? 0);
    const [sth, sta] = pct(home?.shotsOnTarget ?? 0, away?.shotsOnTarget ?? 0);
    const [ch, ca] = pct(home?.corners ?? 0, away?.corners ?? 0);
    const [oh, oa] = pct(home?.offsides ?? 0, away?.offsides ?? 0);
    const [fh, fa] = pct(home?.fouls ?? 0, away?.fouls ?? 0);
    const [yh, ya] = pct(home?.yellowCards ?? 0, away?.yellowCards ?? 0);
    const [ph, pa] = pct(home?.passes ?? 0, away?.passes ?? 0);
    const [th, ta] = pct(home?.tackles ?? 0, away?.tackles ?? 0);
    const [svh, sva] = pct(home?.saves ?? 0, away?.saves ?? 0);
    return [
      { key: 'poss', label: 'Possession', home: `${hp}%`, away: `${ap}%`, homePct: hp, awayPct: ap },
      { key: 'xg', label: 'xG', home: (home?.xg ?? 0).toString(), away: (away?.xg ?? 0).toString(), homePct: 50, awayPct: 50 },
      { key: 'shots', label: 'Tirs', home: String(home?.shots ?? 0), away: String(away?.shots ?? 0), homePct: sh, awayPct: sa },
      { key: 'ontarget', label: 'Tirs cadrés', home: String(home?.shotsOnTarget ?? 0), away: String(away?.shotsOnTarget ?? 0), homePct: sth, awayPct: sta },
      { key: 'corners', label: 'Corners', home: String(home?.corners ?? 0), away: String(away?.corners ?? 0), homePct: ch, awayPct: ca },
      { key: 'offsides', label: 'Hors-jeux', home: String(home?.offsides ?? 0), away: String(away?.offsides ?? 0), homePct: oh, awayPct: oa },
      { key: 'fouls', label: 'Fautes', home: String(home?.fouls ?? 0), away: String(away?.fouls ?? 0), homePct: fh, awayPct: fa },
      { key: 'yellows', label: 'Cartons jaunes', home: String(home?.yellowCards ?? 0), away: String(away?.yellowCards ?? 0), homePct: yh, awayPct: ya },
      { key: 'passes', label: 'Passes', home: String(home?.passes ?? 0), away: String(away?.passes ?? 0), homePct: ph, awayPct: pa },
      { key: 'tackles', label: 'Tacles', home: String(home?.tackles ?? 0), away: String(away?.tackles ?? 0), homePct: th, awayPct: ta },
      { key: 'saves', label: 'Arrêts', home: String(home?.saves ?? 0), away: String(away?.saves ?? 0), homePct: svh, awayPct: sva },
    ];
  });

  constructor() {}

  ngOnInit(): void {
    this.load();
  }

  ngOnDestroy(): void {
    for (const sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.liveEvents.set([]);
    this.api.matchDetail(this.id).subscribe({
      next: (detail) => {
        this.detail.set(detail);
        this.loading.set(false);
        this.listenLive();
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Impossible de charger ce match.');
      },
    });
  }

  setTab(tab: 'resume' | 'stats' | 'lineups'): void {
    this.tab.set(tab);
  }

  private listenLive(): void {
    const current = this.detail();
    if (!current) return;
    const finished = isFinished(current.match);
    const sub1 = this.live.subscribe<LiveScoreMessage>('/topic/live').subscribe((msg) => {
      const d = this.detail();
      if (!d || msg.matchId !== d.match.id) return;
      this.detail.set({
        ...d,
        match: {
          ...d.match,
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
        },
      });
    });
    const sub2 = this.live.subscribe<LiveEventMessage>('/topic/events').subscribe((msg) => {
      const d = this.detail();
      if (!d || msg.matchId !== d.match.id) return;
      this.liveEvents.update((events) => (events.some((e) => e.id === msg.id) ? events : [...events, msg]));
    });
    if (!finished) {
      this.subscriptions.push(sub1, sub2);
    }
  }

  private teamRef(teamId: string | null): TeamRef | null {
    const detail = this.detail();
    if (!detail || !teamId) return null;
    if (detail.match.homeTeam.id === teamId) return detail.match.homeTeam;
    if (detail.match.awayTeam.id === teamId) return detail.match.awayTeam;
    return null;
  }

  score(): string {
    const m = this.detail()?.match;
    if (!m) return '';
    const h = m.homeScore ?? 0;
    const a = m.awayScore ?? 0;
    if (m.homeScore !== null || m.awayScore !== null) return `${h} - ${a}`;
    return 'VS';
  }

  isGoal(type: string): boolean {
    return ['GOAL', 'OWN_GOAL', 'PENALTY', 'MISSED_PENALTY'].includes(type?.toUpperCase());
  }

  isCard(type: string): boolean {
    return ['YELLOW_CARD', 'RED_CARD', 'SECOND_YELLOW'].includes(type?.toUpperCase());
  }

  isSub(type: string): boolean {
    return type?.toUpperCase() === 'SUBSTITUTION';
  }

  eventIcon(type: string): string {
    const upper = type?.toUpperCase();
    if (this.isGoal(type)) return 'bi bi-circle-fill text-success';
    if (upper === 'YELLOW_CARD' || upper === 'SECOND_YELLOW') return 'bi bi-square-fill text-warning';
    if (upper === 'RED_CARD') return 'bi bi-square-fill text-danger';
    if (this.isSub(type)) return 'bi bi-arrow-repeat text-primary';
    if (upper === 'VAR') return 'bi bi-tv';
    if (upper === 'CORNER') return 'bi bi-bounding-box text-primary';
    if (upper === 'OFFSIDE') return 'bi bi-flag text-muted';
    return 'bi bi-dot';
  }

  eventText(e: MatchEventItem): string {
    const player = e.player?.fullName ?? '?';
    switch (e.eventType?.toUpperCase()) {
      case 'GOAL':
        return `${player} marque un but`;
      case 'PENALTY':
        return `${player} transforme le pénalty`;
      case 'MISSED_PENALTY':
        return `${player} rate le pénalty`;
      case 'OWN_GOAL':
        return `${player} marque contre son camp`;
      case 'YELLOW_CARD':
        return `Carton jaune · ${player}`;
      case 'SECOND_YELLOW':
        return `Deuxième carton jaune · ${player}`;
      case 'RED_CARD':
        return `Carton rouge · ${player}`;
      case 'SUBSTITUTION':
        return `Remplacement : ${e.relatedPlayer?.fullName ?? '?'} → ${player}`;
      case 'VAR':
        return 'Intervention de la VAR';
      case 'CORNER':
        return `Corner · ${player}`;
      case 'OFFSIDE':
        return `Hors-jeu · ${player}`;
      default:
        return e.detail ?? e.eventType ?? 'Événement';
    }
  }
}
