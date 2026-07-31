import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard.component';
import { CrudListComponent } from './components/crud-list.component';
import { ApiKeysComponent } from './components/api-keys.component';
import { LoginComponent } from './components/login.component';
import { MainLayoutComponent } from './layout/main-layout.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'continents', component: CrudListComponent, data: { resource: 'continents' } },
      { path: 'countries', component: CrudListComponent, data: { resource: 'countries' } },
      { path: 'cities', component: CrudListComponent, data: { resource: 'cities' } },
      { path: 'confederations', component: CrudListComponent, data: { resource: 'confederations' } },
      { path: 'competitions', component: CrudListComponent, data: { resource: 'competitions' } },
      { path: 'seasons', component: CrudListComponent, data: { resource: 'seasons' } },
      { path: 'stages', component: CrudListComponent, data: { resource: 'stages' } },
      { path: 'groups', component: CrudListComponent, data: { resource: 'groups' } },
      { path: 'groupteams', component: CrudListComponent, data: { resource: 'groupteams' } },
      { path: 'teamseasonparticipations', component: CrudListComponent, data: { resource: 'teamseasonparticipations' } },
      { path: 'rounds', component: CrudListComponent, data: { resource: 'rounds' } },
      { path: 'teams', component: CrudListComponent, data: { resource: 'teams' } },
      { path: 'stadiums', component: CrudListComponent, data: { resource: 'stadiums' } },
      { path: 'referees', component: CrudListComponent, data: { resource: 'referees' } },
      { path: 'coaches', component: CrudListComponent, data: { resource: 'coaches' } },
      { path: 'teamcoachs', component: CrudListComponent, data: { resource: 'teamcoachs' } },
      { path: 'players', component: CrudListComponent, data: { resource: 'players' } },
      { path: 'playerseasonregistrations', component: CrudListComponent, data: { resource: 'playerseasonregistrations' } },
      { path: 'playerseasonstats', component: CrudListComponent, data: { resource: 'playerseasonstats' } },
      { path: 'contracts', component: CrudListComponent, data: { resource: 'contracts' } },
      { path: 'transfers', component: CrudListComponent, data: { resource: 'transfers' } },
      { path: 'matches', component: CrudListComponent, data: { resource: 'matches' } },
      { path: 'lineups', component: CrudListComponent, data: { resource: 'lineups' } },
      { path: 'matchformations', component: CrudListComponent, data: { resource: 'matchformations' } },
      { path: 'matchevents', component: CrudListComponent, data: { resource: 'matchevents' } },
      { path: 'matchpenaltyshootoutshots', component: CrudListComponent, data: { resource: 'matchpenaltyshootoutshots' } },
      { path: 'matchreferees', component: CrudListComponent, data: { resource: 'matchreferees' } },
      { path: 'matchstatisticsteams', component: CrudListComponent, data: { resource: 'matchstatisticsteams' } },
      { path: 'matchstatisticsplayers', component: CrudListComponent, data: { resource: 'matchstatisticsplayers' } },
      { path: 'standings', component: CrudListComponent, data: { resource: 'standings' } },
      { path: 'injuries', component: CrudListComponent, data: { resource: 'injurys' } },
      { path: 'suspensions', component: CrudListComponent, data: { resource: 'suspensions' } },
      { path: 'trophies', component: CrudListComponent, data: { resource: 'trophies' } },
      { path: 'teamtrophys', component: CrudListComponent, data: { resource: 'teamtrophys' } },
      { path: 'playerawards', component: CrudListComponent, data: { resource: 'playerawards' } },
      { path: 'head-to-head', component: CrudListComponent, data: { resource: 'head-to-head' } },
      { path: 'bookmakers', component: CrudListComponent, data: { resource: 'bookmakers' } },
      { path: 'odds', component: CrudListComponent, data: { resource: 'odds' } },
      { path: 'oddhistorys', component: CrudListComponent, data: { resource: 'oddhistorys' } },
      { path: 'news', component: CrudListComponent, data: { resource: 'news' } },
      { path: 'medias', component: CrudListComponent, data: { resource: 'medias' } },
      { path: 'sponsors', component: CrudListComponent, data: { resource: 'sponsors' } },
      { path: 'sponsorlinks', component: CrudListComponent, data: { resource: 'sponsorlinks' } },
      { path: 'translations', component: CrudListComponent, data: { resource: 'translations' } },
      { path: 'api-users', component: CrudListComponent, data: { resource: 'api-users' } },
      { path: 'api-keys', component: ApiKeysComponent },
      { path: 'ratelimits', component: CrudListComponent, data: { resource: 'ratelimits' } },
      { path: 'audit-logs', component: CrudListComponent, data: { resource: 'audit-logs' } },
      { path: '**', redirectTo: '/dashboard' }
    ]
  }
];
