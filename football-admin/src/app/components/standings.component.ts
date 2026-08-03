import { Component, OnInit, OnDestroy, ChangeDetectorRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ApiService } from '../services/api.service';
import { ToastService } from '../services/toast.service';
import { GlobalFilterService } from '../services/global-filter.service';
import { getEntityConfig, FormFieldDef } from '../models/entity-config';
import { FormModalComponent } from './form-modal.component';

interface StandingRow {
  id?: string;
  rankPosition: number;
  teamId: string;
  teamName?: string;
  team?: { id: string; name: string };
  played: number;
  wins: number;
  draws: number;
  losses: number;
  goalsFor: number;
  goalsAgainst: number;
  goalDifference: number;
  points: number;
  form: string | null;
}

@Component({
  selector: 'app-standings',
  standalone: true,
  imports: [FormsModule, FormModalComponent],
  template: `
    <div class="container-fluid py-4">
      <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
        <h2 class="mb-0"><i class="bi bi-ranking-star"></i> Classements</h2>
        <span class="text-muted small">Calcul automatique à partir des matchs du groupe</span>
      </div>

      <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
          <div class="d-flex flex-wrap align-items-end gap-2">
            <div class="flex-grow-1" style="max-width:420px">
              <label class="form-label small text-muted mb-1">Groupe</label>
              <select class="form-select" [(ngModel)]="selectedGroupId" (ngModelChange)="onGroupChange()">
                <option [ngValue]="''">Sélectionnez un groupe…</option>
                @for (g of groups; track g.id) {
                  <option [ngValue]="g.id">{{ groupLabel(g) }}</option>
                }
              </select>
            </div>
            <button class="btn btn-primary" (click)="calculate()" [disabled]="!selectedGroupId || calculating">
              @if (calculating) { <span class="spinner-border spinner-border-sm me-1"></span> }
              <i class="bi bi-calculator me-1"></i> Calculer
            </button>
            @if (calculatedRows) {
              <button class="btn btn-success" (click)="save()" [disabled]="saving">
                @if (saving) { <span class="spinner-border spinner-border-sm me-1"></span> }
                <i class="bi bi-save me-1"></i> Enregistrer
              </button>
            }
            <button class="btn btn-outline-primary" (click)="openAddManual()">
              <i class="bi bi-plus-lg me-1"></i> Ajouter manuellement
            </button>
          </div>

          @if (error) {
            <div class="alert alert-danger py-2 mt-3 mb-0">
              <i class="bi bi-exclamation-triangle me-1"></i>{{ error }}
            </div>
          }
        </div>
      </div>

      @if (loadingSaved) {
        <div class="text-center py-5 text-muted">
          <div class="spinner-border text-primary" role="status"></div>
          <p class="mt-2 mb-0">Chargement du classement enregistré…</p>
        </div>
      }

      @if (calculatedRows && calculatedRows.length) {
        <div class="card shadow-sm border-0 mb-4">
          <div class="card-header bg-white border-0 d-flex align-items-center gap-2">
            <span class="badge text-bg-warning">Calculé</span>
            <span class="small text-muted">Aperçu à partir des matchs avec un score — non enregistré</span>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th class="text-center" style="width:50px">#</th>
                    <th>Équipe</th>
                    <th class="text-center">J</th>
                    <th class="text-center">G</th>
                    <th class="text-center">N</th>
                    <th class="text-center">P</th>
                    <th class="text-center">BP</th>
                    <th class="text-center">BC</th>
                    <th class="text-center">Diff</th>
                    <th class="text-center">Pts</th>
                    <th class="text-center">Forme</th>
                  </tr>
                </thead>
                <tbody>
                  @for (r of calculatedRows; track r.teamId) {
                    <tr>
                      <td class="text-center fw-bold">{{ r.rankPosition }}</td>
                      <td class="fw-semibold">{{ teamName(r) }}</td>
                      <td class="text-center">{{ r.played }}</td>
                      <td class="text-center">{{ r.wins }}</td>
                      <td class="text-center">{{ r.draws }}</td>
                      <td class="text-center">{{ r.losses }}</td>
                      <td class="text-center">{{ r.goalsFor }}</td>
                      <td class="text-center">{{ r.goalsAgainst }}</td>
                      <td class="text-center">{{ r.goalDifference }}</td>
                      <td class="text-center fw-bold">{{ r.points }}</td>
                      <td class="text-center">
                        @for (c of formBadges(r.form); track $index) {
                          <span class="badge me-1 form-badge"
                            [class.text-bg-success]="c === 'V'"
                            [class.text-bg-secondary]="c === 'N'"
                            [class.text-bg-danger]="c === 'D'">{{ c }}</span>
                        }
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        </div>
      } @else if (calculatedRows && !calculatedRows.length) {
        <div class="alert alert-info">
          <i class="bi bi-info-circle me-1"></i>
          Aucun match avec un score trouvé pour ce groupe. Vérifiez que les matchs sont rattachés au groupe et ont un score renseigné (homeScore / awayScore).
        </div>
      }

      @if (savedRows.length) {
        <div class="card shadow-sm border-0">
          <div class="card-header bg-white border-0 d-flex align-items-center gap-2">
            <span class="badge text-bg-success">Enregistré</span>
            <span class="small text-muted">Classement actuellement en base pour ce groupe</span>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th class="text-center" style="width:50px">#</th>
                    <th>Équipe</th>
                    <th class="text-center">J</th>
                    <th class="text-center">G</th>
                    <th class="text-center">N</th>
                    <th class="text-center">P</th>
                    <th class="text-center">BP</th>
                    <th class="text-center">BC</th>
                    <th class="text-center">Diff</th>
                    <th class="text-center">Pts</th>
                    <th class="text-center">Forme</th>
                    <th class="text-end" style="width:110px">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  @for (r of savedRows; track r.id) {
                    <tr>
                      <td class="text-center fw-bold">{{ r.rankPosition }}</td>
                      <td class="fw-semibold">{{ teamName(r) }}</td>
                      <td class="text-center">{{ r.played }}</td>
                      <td class="text-center">{{ r.wins }}</td>
                      <td class="text-center">{{ r.draws }}</td>
                      <td class="text-center">{{ r.losses }}</td>
                      <td class="text-center">{{ r.goalsFor }}</td>
                      <td class="text-center">{{ r.goalsAgainst }}</td>
                      <td class="text-center">{{ r.goalDifference }}</td>
                      <td class="text-center fw-bold">{{ r.points }}</td>
                      <td class="text-center">
                        @for (c of formBadges(r.form); track $index) {
                          <span class="badge me-1 form-badge"
                            [class.text-bg-success]="c === 'V'"
                            [class.text-bg-secondary]="c === 'N'"
                            [class.text-bg-danger]="c === 'D'">{{ c }}</span>
                        }
                      </td>
                      <td class="text-end text-nowrap">
                        <button class="btn btn-sm btn-outline-primary" title="Modifier" (click)="openEditManual(r)">
                          <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger" title="Supprimer" (click)="askDeleteManual(r)">
                          <i class="bi bi-trash"></i>
                        </button>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          </div>
        </div>
      } @else if (selectedGroupId && !loadingSaved) {
        <div class="text-center py-5 text-muted">
          <i class="bi bi-inbox" style="font-size:3rem"></i>
          <p class="mt-2 mb-0">Aucun classement enregistré pour ce groupe.</p>
        </div>
      }
    </div>

    @if (showManualModal) {
      <app-form-modal #manualModal [title]="manualTitle" [fields]="manualFields" [model]="manualModel"
        [submitLabel]="manualEditId ? 'Mettre à jour' : 'Créer'"
        (save)="onManualSave($event)" (cancel)="closeManualModal()"></app-form-modal>
    }

    @if (deleteTarget) {
      <div class="modal fade show d-block" tabindex="-1" role="dialog">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Confirmer la suppression</h5>
              <button type="button" class="btn-close" (click)="cancelDelete()"></button>
            </div>
            <div class="modal-body">
              Supprimer la ligne {{ deleteTarget.rankPosition }} ({{ teamName(deleteTarget) }}) du classement ?
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-sm btn-outline-secondary" (click)="cancelDelete()">Annuler</button>
              <button type="button" class="btn btn-sm btn-danger" (click)="confirmDelete()">Supprimer</button>
            </div>
          </div>
        </div>
      </div>
    }
  `,
  styles: [`
    .form-badge { font-size: .68rem; padding: .22rem .42rem; border-radius: .25rem; }
  `]
})
export class StandingsComponent implements OnInit, OnDestroy {
  groups: any[] = [];
  selectedGroupId = '';
  loadingGroups = false;

  savedRows: StandingRow[] = [];
  calculatedRows: StandingRow[] | null = null;
  loadingSaved = false;
  calculating = false;
  saving = false;
  error = '';

  manualFields: FormFieldDef[] = getEntityConfig('standings')?.formFields || [];
  showManualModal = false;
  manualTitle = '';
  manualEditId: string | null = null;
  manualModel: any = {};
  deleteTarget: StandingRow | null = null;
  deleting = false;

  @ViewChild('manualModal') manualModalComponent?: FormModalComponent;

  private destroy$ = new Subject<void>();

  constructor(private api: ApiService, private toast: ToastService,
              private globalFilter: GlobalFilterService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadGroups();
  }

  loadGroups() {
    this.loadingGroups = true;
    const params = this.globalFilter.resourceFilterParams('groups');
    this.api.getAllArray<any>('groups', { size: 200, ...params }).pipe(takeUntil(this.destroy$)).subscribe({
      next: items => {
        this.groups = items.sort((a, b) => String(a.name || '').localeCompare(String(b.name || '')));
        this.loadingGroups = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.loadingGroups = false;
        this.cdr.detectChanges();
      }
    });
  }

  onGroupChange() {
    this.savedRows = [];
    this.calculatedRows = null;
    this.error = '';
    if (this.selectedGroupId) {
      this.loadSaved();
    }
    this.cdr.detectChanges();
  }

  loadSaved() {
    this.loadingSaved = true;
    this.error = '';
    this.api.getAllArray<any>('standings', { groupId: this.selectedGroupId, size: 200 })
      .pipe(takeUntil(this.destroy$)).subscribe({
        next: items => {
          this.savedRows = items.map(i => ({ ...i, teamName: i.team?.name }));
          this.loadingSaved = false;
          this.cdr.detectChanges();
        },
        error: err => {
          this.error = err.message;
          this.loadingSaved = false;
          this.cdr.detectChanges();
        }
      });
  }

  calculate() {
    if (!this.selectedGroupId) return;
    this.calculating = true;
    this.error = '';
    this.api.calculateStandings<any>(this.selectedGroupId).pipe(takeUntil(this.destroy$)).subscribe({
      next: rows => {
        this.calculatedRows = rows;
        this.calculating = false;
        this.toast.show(`Classement calculé (${rows.length} équipe${rows.length > 1 ? 's' : ''})`, 'success');
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.calculating = false;
        this.cdr.detectChanges();
      }
    });
  }

  save() {
    if (!this.selectedGroupId) return;
    this.saving = true;
    this.error = '';
    this.api.saveStandings<any>(this.selectedGroupId).pipe(takeUntil(this.destroy$)).subscribe({
      next: rows => {
        this.saving = false;
        this.toast.show(`Classement enregistré (${rows.length} ligne${rows.length > 1 ? 's' : ''})`, 'success');
        this.calculatedRows = rows;
        this.loadSaved();
      },
      error: err => {
        this.error = err.message;
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  groupLabel(g: any): string {
    const stage = g.stage?.name;
    return stage ? `${g.name} · ${stage}` : g.name;
  }

  teamName(r: StandingRow): string {
    return r.teamName || r.team?.name || r.teamId;
  }

  formBadges(form: string | null): string[] {
    return (form || '').split('');
  }

  openAddManual() {
    const group = this.groups.find(g => g.id === this.selectedGroupId);
    this.manualModel = {};
    if (group) {
      this.manualModel.groupId = group.id;
      this.manualModel.stageId = group.stage?.id;
      this.manualModel.seasonId = group.stage?.season?.id;
    }
    this.manualEditId = null;
    this.manualTitle = 'Ajouter une ligne de classement';
    this.showManualModal = true;
    this.cdr.detectChanges();
  }

  openEditManual(r: StandingRow) {
    this.manualModel = { ...r };
    this.manualEditId = r.id ?? null;
    this.manualTitle = 'Modifier la ligne de classement';
    this.showManualModal = true;
    this.cdr.detectChanges();
  }

  closeManualModal() {
    this.showManualModal = false;
    this.manualEditId = null;
    this.cdr.detectChanges();
  }

  onManualSave(data: any) {
    const payload = this.buildPayload(data);
    const obs = this.manualEditId
      ? this.api.update('standings', this.manualEditId, payload)
      : this.api.create('standings', payload);
    obs.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.toast.show(this.manualEditId ? 'Ligne de classement mise à jour' : 'Ligne de classement créée', 'success');
        this.showManualModal = false;
        this.manualEditId = null;
        this.loadSaved();
        this.cdr.detectChanges();
      },
      error: err => {
        this.toast.show('Erreur: ' + err.message, 'danger');
        this.manualModalComponent?.resetSubmitting();
        this.cdr.detectChanges();
      }
    });
  }

  private buildPayload(data: any): any {
    const payload: any = {};
    for (const field of this.manualFields) {
      if (field.name in data && data[field.name] !== undefined && data[field.name] !== null) {
        payload[field.name] = data[field.name];
      }
    }
    return payload;
  }

  askDeleteManual(r: StandingRow) {
    this.deleteTarget = r;
    this.cdr.detectChanges();
  }

  cancelDelete() {
    this.deleteTarget = null;
    this.cdr.detectChanges();
  }

  confirmDelete() {
    if (!this.deleteTarget || !this.deleteTarget.id) return;
    this.deleting = true;
    this.api.delete('standings', this.deleteTarget.id).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.toast.show('Ligne de classement supprimée', 'success');
        this.deleteTarget = null;
        this.deleting = false;
        this.loadSaved();
        this.cdr.detectChanges();
      },
      error: err => {
        this.toast.show('Erreur: ' + err.message, 'danger');
        this.deleting = false;
        this.cdr.detectChanges();
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
