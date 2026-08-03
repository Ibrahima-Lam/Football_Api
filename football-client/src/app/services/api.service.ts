import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CoachItem,
  CompetitionDetail,
  CompetitionRef,
  MatchCard,
  MatchDetail,
  MatchEventItem,
  NewsItem,
  PageInfo,
  PlayerSeasonStatItem,
  RefereeItem,
  SeasonRef,
  SquadPlayerItem,
  StandingItem,
  TeamDetail,
  TeamInjuryItem,
  TeamRef,
  TeamSuspensionItem,
} from '../models/models';

export interface MatchQuery {
  date?: string;
  seasonId?: string;
  competitionId?: string;
  teamId?: string;
  live?: boolean;
  page?: number;
  size?: number;
}

export interface MatchEventPayload {
  minute: number;
  extraMinute?: number | null;
  period: string;
  teamId: string;
  playerId?: string | null;
  relatedPlayerId?: string | null;
  eventType: string;
  detail?: string | null;
  comments?: string | null;
  varReviewed?: boolean;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = '/api/client';

  constructor(private http: HttpClient) {}

  matches(query: MatchQuery = {}): Observable<PageInfo<MatchCard>> {
    let params = new HttpParams();
    if (query.date) params = params.set('date', query.date);
    if (query.seasonId) params = params.set('seasonId', query.seasonId);
    if (query.competitionId) params = params.set('competitionId', query.competitionId);
    if (query.teamId) params = params.set('teamId', query.teamId);
    if (query.live) params = params.set('live', 'true');
    if (query.page !== undefined) params = params.set('page', String(query.page));
    if (query.size !== undefined) params = params.set('size', String(query.size));
    return this.http.get<PageInfo<MatchCard>>(`${this.base}/matches`, { params });
  }

  matchDetail(id: string): Observable<MatchDetail> {
    return this.http.get<MatchDetail>(`${this.base}/matches/${id}`);
  }

  addMatchEvent(id: string, payload: MatchEventPayload): Observable<MatchEventItem> {
    return this.http.post<MatchEventItem>(`${this.base}/matches/${id}/events`, payload);
  }

  competitions(): Observable<CompetitionRef[]> {
    return this.http.get<CompetitionRef[]>(`${this.base}/competitions`);
  }

  competitionDetail(id: string): Observable<CompetitionDetail> {
    return this.http.get<CompetitionDetail>(`${this.base}/competitions/${id}`);
  }

  seasons(competitionId: string): Observable<SeasonRef[]> {
    return this.http.get<SeasonRef[]>(`${this.base}/seasons`, { params: { competitionId } });
  }

  standings(seasonId: string, stageId?: string): Observable<StandingItem[]> {
    let params = new HttpParams().set('seasonId', seasonId);
    if (stageId) params = params.set('stageId', stageId);
    return this.http.get<StandingItem[]>(`${this.base}/standings`, { params });
  }

  teams(seasonId?: string): Observable<TeamRef[]> {
    let params = new HttpParams();
    if (seasonId) params = params.set('seasonId', seasonId);
    return this.http.get<TeamRef[]>(`${this.base}/teams`, { params });
  }

  teamDetail(id: string): Observable<TeamDetail> {
    return this.http.get<TeamDetail>(`${this.base}/teams/${id}`);
  }

  teamPlayers(id: string, seasonId?: string): Observable<SquadPlayerItem[]> {
    let params = new HttpParams();
    if (seasonId) params = params.set('seasonId', seasonId);
    return this.http.get<SquadPlayerItem[]>(`${this.base}/teams/${id}/players`, { params });
  }

  teamStats(id: string, seasonId?: string): Observable<PlayerSeasonStatItem[]> {
    let params = new HttpParams();
    if (seasonId) params = params.set('seasonId', seasonId);
    return this.http.get<PlayerSeasonStatItem[]>(`${this.base}/teams/${id}/stats`, { params });
  }

  teamSuspensions(id: string): Observable<TeamSuspensionItem[]> {
    return this.http.get<TeamSuspensionItem[]>(`${this.base}/teams/${id}/suspensions`);
  }

  teamInjuries(id: string): Observable<TeamInjuryItem[]> {
    return this.http.get<TeamInjuryItem[]>(`${this.base}/teams/${id}/injuries`);
  }

  playerSeasonStats(seasonId: string, stat: 'scorers' | 'assists' | 'cards'): Observable<PlayerSeasonStatItem[]> {
    return this.http.get<PlayerSeasonStatItem[]>(`${this.base}/player-stats`, { params: { seasonId, stat } });
  }

  news(competitionId?: string, teamId?: string): Observable<NewsItem[]> {
    let params = new HttpParams();
    if (competitionId) params = params.set('competitionId', competitionId);
    if (teamId) params = params.set('teamId', teamId);
    return this.http.get<NewsItem[]>(`${this.base}/news`, { params });
  }

  seasonReferees(seasonId: string): Observable<RefereeItem[]> {
    return this.http.get<RefereeItem[]>(`${this.base}/referees`, { params: { seasonId } });
  }

  seasonCoaches(seasonId: string): Observable<CoachItem[]> {
    return this.http.get<CoachItem[]>(`${this.base}/coaches`, { params: { seasonId } });
  }
}
