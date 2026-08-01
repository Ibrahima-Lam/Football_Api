import { Component, OnInit, OnDestroy, signal, ChangeDetectorRef } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { GlobalFilterService } from '../services/global-filter.service';
import { AuthService } from '../services/auth.service';
import { FcmService } from '../services/fcm.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit, OnDestroy {
  filterCompetitionId = signal('');
  filterSeasonId = signal('');
  userMenuOpen = signal(false);
  private destroy$ = new Subject<void>();

  constructor(public globalFilter: GlobalFilterService, private auth: AuthService, private cdr: ChangeDetectorRef, private fcm: FcmService) {}

  ngOnInit() {
    this.fcm.init();
    this.globalFilter.loadCompetitions().subscribe(() => this.cdr.detectChanges());
    this.globalFilter.state$.pipe(takeUntil(this.destroy$)).subscribe(s => {
      if (!s.competitionId && !s.seasonId) {
        this.filterCompetitionId.set('');
        this.filterSeasonId.set('');
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get competitions() {
    return this.globalFilter.competitions;
  }

  get seasons() {
    return this.globalFilter.seasons;
  }

  get currentUser() {
    return this.auth.currentUser();
  }

  onCompetitionChange(id: string) {
    this.filterCompetitionId.set(id || '');
    this.filterSeasonId.set('');
    this.globalFilter.setCompetition(id || '')?.subscribe(() => this.cdr.detectChanges());
  }

  onSeasonChange(id: string) {
    this.filterSeasonId.set(id || '');
    this.globalFilter.setSeason(id || '');
    this.cdr.detectChanges();
  }

  clearGlobalFilter() {
    this.globalFilter.clear();
    this.filterCompetitionId.set('');
    this.filterSeasonId.set('');
  }

  toggleUserMenu() {
    this.userMenuOpen.update(v => !v);
  }

  logout() {
    this.userMenuOpen.set(false);
    this.fcm.disable().then(() => this.auth.logout());
  }
}
