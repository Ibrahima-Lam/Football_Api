import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, shareReplay } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiService } from './api.service';
import { EntityScope } from '../models/entity-config';

export interface GlobalFilterState {
  competitionId: string;
  seasonId: string;
}

@Injectable({
  providedIn: 'root'
})
export class GlobalFilterService {
  private state = new BehaviorSubject<GlobalFilterState>({ competitionId: '', seasonId: '' });
  state$ = this.state.asObservable();

  competitions: any[] = [];
  seasons: any[] = [];
  competitionLabel = '';
  seasonLabel = '';

  constructor(private api: ApiService) {}

  get current(): GlobalFilterState {
    return this.state.getValue();
  }

  get active(): boolean {
    return !!this.current.competitionId;
  }

  get label(): string {
    const parts: string[] = [];
    if (this.competitionLabel) parts.push(this.competitionLabel);
    if (this.seasonLabel) parts.push(this.seasonLabel);
    return parts.join(' · ');
  }

  loadCompetitions(): Observable<any[]> {
    const obs = this.api.getAllArray<any>('competitions', { size: 200 }).pipe(
      map(items => items.sort((a, b) => String(a.name || '').localeCompare(String(b.name || '')))),
      shareReplay(1)
    );
    obs.subscribe(items => {
      this.competitions = items;
    });
    return obs;
  }

  setCompetition(competitionId: string): Observable<any[]> | null {
    const comp = this.competitions.find(c => c.id === competitionId);
    this.competitionLabel = comp ? comp.name : '';
    this.seasonLabel = '';
    this.seasons = [];
    this.state.next({ competitionId, seasonId: '' });
    if (competitionId) {
      const obs = this.api.getAllArray<any>('seasons', { size: 200, competitionId }).pipe(
        map(items => items.sort((a, b) => String(a.name || '').localeCompare(String(b.name || '')))),
        shareReplay(1)
      );
      obs.subscribe(items => {
        this.seasons = items;
      });
      return obs;
    }
    return null;
  }

  setSeason(seasonId: string) {
    const season = this.seasons.find(s => s.id === seasonId);
    this.seasonLabel = season ? season.name : '';
    this.state.next({ competitionId: this.current.competitionId, seasonId });
  }

  clear() {
    this.competitionLabel = '';
    this.seasonLabel = '';
    this.seasons = [];
    this.state.next({ competitionId: '', seasonId: '' });
  }

  buildGlobalParams(scope: EntityScope): Record<string, string> {
    const params: Record<string, string> = {};
    const s = this.current;
    if (scope.season && s.seasonId) {
      params['seasonId'] = s.seasonId;
    } else if (scope.competition && s.competitionId) {
      params['competitionId'] = s.competitionId;
    } else if (scope.season && s.competitionId) {
      params['season.competition.id'] = s.competitionId;
    }
    return params;
  }

  resourceFilterParams(resource: string): Record<string, string> {
    const { competitionId, seasonId } = this.current;
    const params: Record<string, string> = {};
    if (!competitionId && !seasonId) return params;
    switch (resource) {
      case 'seasons':
        if (seasonId) params['id'] = seasonId;
        else params['competitionId'] = competitionId;
        break;
      case 'stages':
        if (seasonId) params['seasonId'] = seasonId;
        else params['season.competition.id'] = competitionId;
        break;
      case 'rounds':
      case 'groups':
        if (seasonId) params['stage.season.id'] = seasonId;
        else params['stage.season.competition.id'] = competitionId;
        break;
      case 'matches':
        if (seasonId) params['seasonId'] = seasonId;
        else params['season.competition.id'] = competitionId;
        break;
      case 'trophies':
        if (competitionId) params['competitionId'] = competitionId;
        break;
    }
    return params;
  }
}
