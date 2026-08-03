import { Component, Input, inject } from '@angular/core';
import { Router } from '@angular/router';
import { MatchCard } from '../models/models';
import { fmtTime, isLive, isFinished, statusLabel, scoreSub, displayScore } from '../utils';

@Component({
  selector: 'app-match-list',
  standalone: true,
  template: `
    @if (matches.length === 0) {
      <div class="fc-panel text-center py-5 text-muted">
        <i class="bi bi-calendar-x display-6 d-block mb-2"></i>
        <div class="fw-semibold">Aucun match pour ce jour</div>
        <div class="small">Essayez une autre date ou modifiez les filtres.</div>
      </div>
    } @else {
      <div>
        @for (m of matches; track m.id) {
          <div class="fc-match-row fc-card-hover rounded-3 mb-2 border border-light-subtle" [class.fc-match-live]="isLive(m)" (click)="open(m)">
            <div class="text-center" style="min-width: 64px">
              @if (isLive(m)) {
                <span class="fc-badge fc-badge-live"><span class="fc-live-dot"></span>{{ m.minute ?? '' }}'</span>
              } @else if (isFinished(m)) {
                <span class="fc-badge fc-badge-finished"><i class="bi bi-check2"></i>{{ statusLabel(m.status) }}</span>
              } @else {
                <div class="fw-bold text-muted">{{ fmtTime(m.kickoff) }}</div>
                <div class="small text-muted">{{ m.stageName || '—' }}</div>
              }
            </div>

            <div class="fc-team right">
              <span class="fc-team-name">{{ m.homeTeam.name }}</span>
              @if (m.homeTeam.kitPrimaryColor) {
                <span class="fc-kit" [style.background]="m.homeTeam.kitPrimaryColor"></span>
              }
              @if (m.homeTeam.logo) {
                <img class="fc-crest" [src]="m.homeTeam.logo" [alt]="m.homeTeam.name" loading="lazy" />
              }
            </div>

            <div class="fc-score">
              @if (isLive(m) || isFinished(m) || m.homeScore !== null || m.awayScore !== null) {
                <span class="fs-5">
                  {{ displayScore(m.status, m.homeScore, m.homeHtScore, m.homeEtScore, m.homePenaltyScore) }}
                  <span class="mx-1 text-muted">-</span>
                  {{ displayScore(m.status, m.awayScore, m.awayHtScore, m.awayEtScore, m.awayPenaltyScore) }}
                </span>
                @if (scoreSub(m)) {
                  <div class="sub">{{ scoreSub(m) }}</div>
                }
                @if (m.homePenaltyForm || m.awayPenaltyForm) {
                  <div class="sub fc-pen-form">
                    {{ m.homePenaltyForm ?? '—' }}<span class="mx-1 text-muted">·</span>{{ m.awayPenaltyForm ?? '—' }}
                  </div>
                }
              } @else {
                <span class="text-muted fw-bold">VS</span>
              }
            </div>

            <div class="fc-team">
              @if (m.awayTeam.logo) {
                <img class="fc-crest" [src]="m.awayTeam.logo" [alt]="m.awayTeam.name" loading="lazy" />
              }
              @if (m.awayTeam.kitPrimaryColor) {
                <span class="fc-kit" [style.background]="m.awayTeam.kitPrimaryColor"></span>
              }
              <span class="fc-team-name">{{ m.awayTeam.name }}</span>
            </div>

            <i class="bi bi-chevron-right text-muted"></i>
          </div>
        }
      </div>
    }
  `,
})
export class MatchListComponent {
  @Input({ required: true }) matches: MatchCard[] = [];

  private router = inject(Router);

  isLive = isLive;
  isFinished = isFinished;
  statusLabel = statusLabel;
  fmtTime = fmtTime;
  displayScore = displayScore;
  scoreSub = scoreSub;

  open(m: MatchCard): void {
    this.router.navigate(['/match', m.id]);
  }
}
