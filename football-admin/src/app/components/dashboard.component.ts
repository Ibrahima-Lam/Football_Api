import { Component, OnInit, OnDestroy, ChangeDetectorRef, NgZone } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, of } from 'rxjs';
import { catchError, takeUntil } from 'rxjs/operators';
import { ApiService } from '../services/api.service';

export const DASHBOARD_ENTITIES: { label: string; route: string }[] = [
  { label: 'Continents', route: '/continents' },
  { label: 'Countries', route: '/countries' },
  { label: 'Cities', route: '/cities' },
  { label: 'Confederations', route: '/confederations' },
  { label: 'Competitions', route: '/competitions' },
  { label: 'Seasons', route: '/seasons' },
  { label: 'Stages', route: '/stages' },
  { label: 'Groups', route: '/groups' },
  { label: 'Group Teams', route: '/groupteams' },
  { label: 'Team Season Participations', route: '/teamseasonparticipations' },
  { label: 'Rounds', route: '/rounds' },
  { label: 'Teams', route: '/teams' },
  { label: 'Stadiums', route: '/stadiums' },
  { label: 'Referees', route: '/referees' },
  { label: 'Coaches', route: '/coaches' },
  { label: 'Team Coaches', route: '/teamcoachs' },
  { label: 'Players', route: '/players' },
  { label: 'Player Season Registrations', route: '/playerseasonregistrations' },
  { label: 'Player Season Stats', route: '/playerseasonstats' },
  { label: 'Contracts', route: '/contracts' },
  { label: 'Transfers', route: '/transfers' },
  { label: 'Matches', route: '/matches' },
  { label: 'Lineups', route: '/lineups' },
  { label: 'Match Formations', route: '/matchformations' },
  { label: 'Match Events', route: '/matchevents' },
  { label: 'Penalty Shootout Shots', route: '/matchpenaltyshootoutshots' },
  { label: 'Match Referees', route: '/matchreferees' },
  { label: 'Match Statistics (Team)', route: '/matchstatisticsteams' },
  { label: 'Match Statistics (Player)', route: '/matchstatisticsplayers' },
  { label: 'Standings', route: '/standings' },
  { label: 'Injuries', route: '/injuries' },
  { label: 'Suspensions', route: '/suspensions' },
  { label: 'Trophies', route: '/trophies' },
  { label: 'Team Trophies', route: '/teamtrophys' },
  { label: 'Player Awards', route: '/playerawards' },
  { label: 'Head to Head', route: '/head-to-head' },
  { label: 'Bookmakers', route: '/bookmakers' },
  { label: 'Odds', route: '/odds' },
  { label: 'Odds History', route: '/oddhistorys' },
  { label: 'News', route: '/news' },
  { label: 'Media', route: '/medias' },
  { label: 'Sponsors', route: '/sponsors' },
  { label: 'Sponsor Links', route: '/sponsorlinks' },
  { label: 'Translations', route: '/translations' },
  { label: 'API Users', route: '/api-users' },
  { label: 'API Keys', route: '/api-keys' },
  { label: 'Rate Limits', route: '/ratelimits' },
  { label: 'Audit Logs', route: '/audit-logs' }
];

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  stats = {
    competitions: 0,
    teams: 0,
    players: 0,
    matches: 0,
    continents: 0,
    countries: 0,
    stadiums: 0,
    coaches: 0
  };
  recentMatches: any[] = [];
  entities = DASHBOARD_ENTITIES;
  loading = true;
  errorMessage = '';
  entitySearch = '';

  private destroy$ = new Subject<void>();

  constructor(
    private api: ApiService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit() {
    this.loadStats();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private done() {
    this.zone.run(() => {
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  loadStats() {
    const resources = ['competitions', 'teams', 'players', 'matches', 'continents', 'countries', 'stadiums', 'coaches'];
    let completed = 0;
    for (const r of resources) {
      this.api.getAll<any>(r, { page: 0, size: r === 'matches' ? 5 : 1 }).pipe(
        catchError(err => {
          if (!this.errorMessage) this.errorMessage = err.message;
          return of({ content: [], totalElements: 0, totalPages: 0, size: 0, number: 0, first: true, last: true } as any);
        }),
        takeUntil(this.destroy$)
      ).subscribe(res => {
        if (r === 'matches') {
          this.stats.matches = res.totalElements;
          this.recentMatches = res.content;
        } else {
          (this.stats as any)[r] = res.totalElements;
        }
      }).add(() => {
        completed++;
        if (completed >= resources.length) this.done();
      });
    }
    setTimeout(() => this.done(), 15000);
  }

  goToEntity(value: string) {
    const term = (value || '').trim().toLowerCase();
    if (!term) return;
    const match = this.entities.find(e => e.label.toLowerCase() === term);
    if (match) {
      this.entitySearch = '';
      this.router.navigateByUrl(match.route);
      return;
    }
    const startsWith = this.entities.find(e => e.label.toLowerCase().startsWith(term));
    if (startsWith) {
      this.entitySearch = '';
      this.router.navigateByUrl(startsWith.route);
    }
  }
}
