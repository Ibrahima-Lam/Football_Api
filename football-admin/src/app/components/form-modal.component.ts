import { Component, EventEmitter, Input, Output, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable, Subject, forkJoin, of } from 'rxjs';
import { catchError, map, switchMap, takeUntil } from 'rxjs/operators';
import { FormFieldDef, EXCLUDED_FORM_FIELDS } from '../models/entity-config';
import { ApiService } from '../services/api.service';
import { GlobalFilterService, GlobalFilterState } from '../services/global-filter.service';

@Component({
  selector: 'app-form-modal',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="modal-backdrop fade show"></div>
    <div class="modal fade show d-block" tabindex="-1" role="dialog" (click)="cancel.emit()">
      <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered modal-lg" (click)="$event.stopPropagation()">
        <div class="modal-content">
          <div class="modal-header border-0 pb-0">
            <h5 class="modal-title fw-bold">{{ title }}</h5>
            <button type="button" class="btn-close" (click)="cancel.emit()"></button>
          </div>
          <div class="modal-body">
            @if (loadingOptions) {
              <div class="text-center py-3"><div class="spinner-border spinner-border-sm"></div> Loading options...</div>
            }
            <form #f="ngForm">
              <div class="row g-3">
                @for (field of visibleFields; track field.name) {
                  <div class="col-12" [class.col-md-6]="field.type !== 'textarea' && field.colSpan !== 12">
                    <label class="form-label fw-medium small text-muted mb-1">{{ field.label }}@if (field.required) { <span class="text-danger">*</span> }</label>
                    @if (field.type === 'textarea') {
                      <textarea class="form-control" [name]="field.name" [(ngModel)]="model[field.name]"
                        [required]="field.required" [minlength]="field.minLength" [maxlength]="field.maxLength"
                        rows="3" [placeholder]="getFieldPlaceholder(field)"></textarea>
                    } @else if (field.type === 'select') {
                      <select class="form-select" [name]="field.name" [(ngModel)]="model[field.name]"
                        [required]="field.required">
                        <option [ngValue]="null">Sélectionner...</option>
                        @if (field.options) {
                          @for (opt of field.options; track opt.value) {
                            <option [ngValue]="opt.value">{{ opt.label }}</option>
                          }
                        } @else if (field.resource) {
                          @for (opt of optionMap[field.name] || []; track opt.value) {
                            <option [ngValue]="opt.value">{{ opt.label }}</option>
                          }
                        }
                      </select>
                    } @else if (field.type === 'boolean') {
                      <div class="form-check form-switch pt-1">
                        <input class="form-check-input" type="checkbox" [name]="field.name"
                          [(ngModel)]="model[field.name]" [required]="field.required">
                      </div>
                    } @else if (field.type === 'number') {
                      <input class="form-control" type="number" [name]="field.name" [(ngModel)]="model[field.name]"
                        [required]="field.required" [min]="field.min" [max]="field.max" [step]="field.step"
                        [placeholder]="getFieldPlaceholder(field)">
                    } @else if (field.type === 'date' || field.type === 'datetime-local') {
                      <input class="form-control" [type]="field.type" [name]="field.name" [(ngModel)]="model[field.name]"
                        [required]="field.required" [placeholder]="getFieldPlaceholder(field)">
                    } @else if (field.type === 'file') {
                      <div>
                        @if (uploading[field.name]) {
                          <div class="d-flex align-items-center gap-2 text-muted small">
                            <div class="spinner-border spinner-border-sm" role="status"></div>
                            Upload en cours...
                          </div>
                        } @else {
                          @if (model[field.name]) {
                            <div class="d-flex align-items-center gap-2 flex-wrap">
                              @if (field.fileKind === 'video') {
                                <video [src]="model[field.name]" class="file-preview" controls></video>
                              } @else {
                                <img [src]="model[field.name]" class="file-preview" alt="">
                              }
                              <div class="flex-grow-1">
                                <code class="small text-break">{{ model[field.name] }}</code>
                                <div class="d-flex gap-2 mt-1">
                                  <label class="btn btn-sm btn-outline-secondary mb-0">
                                    Remplacer
                                    <input type="file" class="d-none"
                                      [accept]="field.fileKind === 'video' ? 'video/*' : 'image/*'"
                                      (change)="onFileSelected(field, $event)">
                                  </label>
                                  <button type="button" class="btn btn-sm btn-outline-danger" (click)="clearFile(field)">
                                    <i class="bi bi-trash me-1"></i>Retirer
                                  </button>
                                </div>
                              </div>
                            </div>
                          } @else {
                            <label class="btn btn-sm btn-outline-primary mb-0">
                              <i class="bi bi-upload me-1"></i> Choisir un fichier
                              <input type="file" class="d-none"
                                [accept]="field.fileKind === 'video' ? 'video/*' : 'image/*'"
                                (change)="onFileSelected(field, $event)">
                            </label>
                          }
                          @if (uploadError[field.name]) {
                            <div class="text-danger small mt-1">{{ uploadError[field.name] }}</div>
                          }
                        }
                      </div>
                    } @else {
                      <input class="form-control" [type]="field.type" [name]="field.name" [(ngModel)]="model[field.name]"
                        [required]="field.required" [minlength]="field.minLength" [maxlength]="field.maxLength"
                        [placeholder]="getFieldPlaceholder(field)">
                    }
                  </div>
                }
              </div>
            </form>
            @if (!loadingOptions && !visibleFields.length) {
              <p class="text-center text-muted py-3 mb-0">No fields available.</p>
            }
          </div>
          <div class="modal-footer border-0 pt-0">
            <button type="button" class="btn btn-sm btn-outline-secondary px-3" (click)="cancel.emit()">Cancel</button>
            <button type="button" class="btn btn-sm btn-primary px-4" (click)="onSubmit()" [disabled]="submitting">
              @if (submitting) { <span class="spinner-border spinner-border-sm me-1"></span> }
              {{ submitLabel }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .modal-backdrop { z-index: 1050; }
    .modal { z-index: 1055; }
    .file-preview { max-height: 96px; max-width: 160px; object-fit: cover; border-radius: 6px; border: 1px solid var(--bs-border-color); }
  `]
})
export class FormModalComponent implements OnInit, OnDestroy {
  @Input() title = '';
  @Input() fields: FormFieldDef[] = [];
  @Input() model: any = {};
  @Input() submitLabel = 'Save';
  @Output() save = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  submitting = false;
  loadingOptions = false;
  optionMap: Record<string, { label: string; value: any }[]> = {};
  uploading: Record<string, boolean> = {};
  uploadError: Record<string, string> = {};

  private destroy$ = new Subject<void>();

  get visibleFields() {
    return this.fields.filter(f => !EXCLUDED_FORM_FIELDS.includes(f.name));
  }

  getFieldPlaceholder(field: FormFieldDef): string {
    if (field.placeholder) return field.placeholder;
    switch (field.type) {
      case 'select': return 'Sélectionner...';
      case 'date': return 'AAAA-MM-JJ';
      case 'datetime-local': return 'AAAA-MM-JJ HH:MM';
      case 'number': return 'Nombre';
      default: return 'Saisir ' + field.label.toLowerCase();
    }
  }

  constructor(private api: ApiService, private globalFilter: GlobalFilterService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.normalizeModel();
    this.loadOptions();
  }

  private normalizeModel() {
    for (const field of this.fields) {
      if (!field.resource || !field.name.endsWith('Id')) continue;
      if (this.model[field.name] !== undefined && this.model[field.name] !== null) continue;
      const nested = this.resolveNested(this.model, field.name.slice(0, -2));
      if (nested && typeof nested === 'object' && nested.id !== undefined) {
        this.model[field.name] = nested.id;
      }
    }
  }

  private resolveNested(obj: any, path: string): any {
    return path.split('.').reduce((o, k) => (o && o[k] !== undefined ? o[k] : undefined), obj);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadOptions() {
    const resourceFields = this.fields.filter(f => f.resource && !f.options);
    if (!resourceFields.length) return;
    this.loadingOptions = true;
    const requests = resourceFields.map(field => this.loadFieldOptions(field, this.globalFilter.current));
    forkJoin(requests).pipe(takeUntil(this.destroy$)).subscribe(results => {
      resourceFields.forEach((field, i) => {
        this.optionMap[field.name] = results[i];
      });
      this.loadingOptions = false;
      this.cdr.detectChanges();
    });
  }

  private loadFieldOptions(field: FormFieldDef, current: GlobalFilterState): Observable<{ label: string; value: any }[]> {
    const df = field.displayField || 'name';
    const vf = field.valueField || 'id';
    const toOptions = (items: any[]) => items.map(item => ({ label: item[df] || item[vf], value: item[vf] }));
    if (field.resource === 'teams' && (current.competitionId || current.seasonId)) {
      const partParams: any = { size: 200 };
      if (current.seasonId) partParams.seasonId = current.seasonId;
      else partParams['season.competition.id'] = current.competitionId;
      return this.api.getAllArray<any>('teamseasonparticipations', partParams).pipe(
        map(parts => parts.map(p => p.team?.id).filter(Boolean)),
        switchMap(ids => this.api.getAllArray<any>('teams', { size: 200 }).pipe(
          map(teams => teams.filter(t => ids.includes(t.id)))
        )),
        map(toOptions),
        catchError(() => of([]))
      );
    }
    const params = this.globalFilter.resourceFilterParams(field.resource!);
    return this.api.getAllArray<any>(field.resource!, { size: 200, ...params }).pipe(
      map(toOptions),
      catchError(() => of([]))
    );
  }

  resetSubmitting() {
    this.submitting = false;
    this.cdr.detectChanges();
  }

  onFileSelected(field: FormFieldDef, event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    input.value = '';
    if (!file) return;

    const kind = field.fileKind ?? 'image';
    if (kind === 'image' && !file.type.startsWith('image/')) {
      this.uploadError[field.name] = 'Format image requis (JPG, PNG, GIF, WEBP, SVG)';
      return;
    }
    if (kind === 'video' && !file.type.startsWith('video/')) {
      this.uploadError[field.name] = 'Format vidéo requis (MP4, WEBM)';
      return;
    }

    this.uploading[field.name] = true;
    this.uploadError[field.name] = '';
    this.api.upload(file).pipe(takeUntil(this.destroy$)).subscribe({
      next: res => {
        this.model[field.name] = res.url;
        this.uploading[field.name] = false;
        this.cdr.detectChanges();
      },
      error: (e: any) => {
        this.uploading[field.name] = false;
        this.uploadError[field.name] = e?.error?.message ?? e?.message ?? "Échec de l'upload";
        this.cdr.detectChanges();
      }
    });
  }

  clearFile(field: FormFieldDef) {
    this.model[field.name] = null;
    this.uploadError[field.name] = '';
  }

  onSubmit() {
    if (this.submitting) return;
    this.submitting = true;
    this.save.emit({ ...this.model });
  }
}
