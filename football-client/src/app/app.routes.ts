import { Routes } from '@angular/router';
import { HomeComponent } from './components/home';

export const routes: Routes = [
  { path: '', pathMatch: 'full', component: HomeComponent, data: { tab: 'matches' } },
  { path: 'equipes', component: HomeComponent, data: { tab: 'teams' } },
  { path: 'actualites', component: HomeComponent, data: { tab: 'news' } },
  { path: 'match/:id', loadComponent: () => import('./components/match-detail').then((m) => m.MatchDetailComponent) },
  { path: 'competitions', loadComponent: () => import('./components/competitions').then((m) => m.CompetitionsComponent) },
  { path: 'competition/:id', loadComponent: () => import('./components/competition-detail').then((m) => m.CompetitionDetailComponent) },
  { path: 'team/:id', loadComponent: () => import('./components/team-detail').then((m) => m.TeamDetailComponent) },
  { path: 'settings', loadComponent: () => import('./components/settings').then((m) => m.SettingsComponent) },
  { path: '**', redirectTo: '' },
];
