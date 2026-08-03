import { Component, OnInit, ViewChild, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe, LowerCasePipe } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ApiService, PageResponse } from '../services/api.service';
import { ToastService } from '../services/toast.service';
import { GlobalFilterService } from '../services/global-filter.service';
import { getEntityConfig, getEntityFilters, getEntitySearchPlaceholder, getEntityScope, ColumnDef, FormFieldDef, FilterDef, EntityScope, EXCLUDED_FORM_FIELDS } from '../models/entity-config';
import { FormModalComponent } from './form-modal.component';

@Component({
  selector: 'app-crud-list',
  standalone: true,
  imports: [FormsModule, DatePipe, LowerCasePipe, FormModalComponent],
  template: `
    <div class="container-fluid py-4">
      <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
        <h2 class="mb-0"><i class="bi" [class]="titleIcon"></i> {{ title }}</h2>
        <button class="btn btn-primary" (click)="openCreate()">
          <i class="bi bi-plus-lg"></i> Add {{ itemName }}
        </button>
      </div>

      <div class="card shadow-sm border-0">
        <div class="card-body">
          <div class="d-flex flex-wrap align-items-end gap-2 mb-3">
            <div class="flex-grow-1" style="min-width:220px; max-width:340px">
              <label class="form-label small text-muted mb-1">Search</label>
              <div class="d-flex gap-1">
                <input class="form-control form-control-sm" [placeholder]="searchPlaceholder"
                  [(ngModel)]="searchTerm" (keyup.enter)="search()">
                <button class="btn btn-sm btn-outline-secondary" title="Search" (click)="search()">
                  <i class="bi bi-search"></i>
                </button>
                <button class="btn btn-sm btn-outline-secondary" title="Clear search" (click)="clearSearch()">
                  <i class="bi bi-x-lg"></i>
                </button>
              </div>
            </div>

            @for (f of filters; track f.name) {
              <div>
                <label class="form-label small text-muted mb-1">{{ f.label }}</label>
                <select class="form-select form-select-sm" [ngModel]="filterValues[f.name] || ''"
                  (ngModelChange)="onFilterChange(f.name, $event)">
                  <option [ngValue]="''">All</option>
                  @if (f.options) {
                    @for (opt of f.options; track opt.value) {
                      <option [ngValue]="opt.value">{{ opt.label }}</option>
                    }
                  } @else if (f.resource) {
                    @for (opt of filterOptions[f.name] || []; track opt.value) {
                      <option [ngValue]="opt.value">{{ opt.label }}</option>
                    }
                  }
                </select>
              </div>
            }

            @if (hasActiveFilters) {
              <div>
                <label class="form-label small text-muted mb-1">&nbsp;</label>
                <div>
                  <button class="btn btn-sm btn-outline-secondary" (click)="clearFilters()">
                    <i class="bi bi-x-circle me-1"></i>Clear
                  </button>
                </div>
              </div>
            }

            <div class="ms-auto">
              <label class="form-label small text-muted mb-1">Per page</label>
              <select class="form-select form-select-sm" [(ngModel)]="pageSize" (change)="loadItems(0)">
                <option [value]="10">10</option>
                <option [value]="20">20</option>
                <option [value]="50">50</option>
                <option [value]="100">100</option>
              </select>
            </div>
          </div>

          @if (errorMessage) {
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="bi bi-exclamation-triangle"></i> {{ errorMessage }}
              <button type="button" class="btn-close" (click)="errorMessage = ''"></button>
            </div>
          }

          @if (hasGlobalFilter) {
            <div class="alert alert-info py-2 d-flex align-items-center gap-2 mb-3" role="alert">
              <i class="bi bi-funnel"></i>
              <span class="small">Global filter: <strong>{{ globalFilterLabel }}</strong></span>
              <button class="btn btn-sm btn-outline-secondary ms-auto" (click)="clearGlobalFilter()">
                <i class="bi bi-x-circle me-1"></i>Clear
              </button>
            </div>
          }

          @if (loading) {
            <div class="text-center py-5">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
              <p class="text-muted mt-2 mb-0">Loading {{ title | lowercase }}...</p>
            </div>
          } @else if (items.length === 0) {
            <div class="text-center py-5 text-muted">
              <i class="bi bi-inbox" style="font-size:3rem"></i>
              <p class="mt-2 mb-0">No {{ title | lowercase }} found.</p>
              @if (searchTerm) {
                <p class="small">Try a different search term.</p>
              }
            </div>
          } @else {
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    @for (col of columns; track col.field) {
                      <th>
                        <span>{{ col.header }}</span>
                      </th>
                    }
                    <th class="text-end" [style.width]="quickFormFields.length ? '150px' : '120px'">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  @for (item of items; track item[idField]) {
                    <tr>
                      @for (col of columns; track col.field) {
                        <td>
                          @if (col.type === 'boolean') {
                            <span class="badge" [class.bg-success]="resolveValue(item, col.field)"
                              [class.bg-secondary]="!resolveValue(item, col.field)">
                              {{ resolveValue(item, col.field) ? 'Yes' : 'No' }}
                            </span>
                          } @else if (col.type === 'date' && resolveValue(item, col.field)) {
                            {{ resolveValue(item, col.field) | date:'medium' }}
                          } @else if (col.type === 'number') {
                            {{ resolveValue(item, col.field) }}
                          } @else {
                            {{ resolveValue(item, col.field) }}
                          }
                        </td>
                      }
                      <td class="text-end">
                        <div class="btn-group btn-group-sm">
                          <button class="btn btn-outline-info" title="View" (click)="viewItem(item)">
                            <i class="bi bi-eye"></i>
                          </button>
                          @if (quickFormFields.length) {
                            <button class="btn btn-outline-success" title="Quick update" (click)="openQuick(item)">
                              <i class="bi bi-lightning-charge"></i>
                            </button>
                          }
                          <button class="btn btn-outline-warning" title="Edit" (click)="editItem(item)">
                            <i class="bi bi-pencil"></i>
                          </button>
                          <button class="btn btn-outline-danger" title="Delete" (click)="deleteItem(item)">
                            <i class="bi bi-trash"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>

            @if (page.totalPages > 1) {
              <div class="d-flex justify-content-between align-items-center mt-3">
                <small class="text-muted">
                  Page {{ page.number + 1 }} of {{ page.totalPages }}
                  ({{ page.totalElements }} total)
                </small>
                <nav>
                  <ul class="pagination pagination-sm mb-0">
                    <li class="page-item" [class.disabled]="page.first">
                      <button class="page-link" (click)="changePage(0)">&laquo;&laquo;</button>
                    </li>
                    <li class="page-item" [class.disabled]="page.first">
                      <button class="page-link" (click)="changePage(page.number - 1)">&laquo;</button>
                    </li>
                    @for (pn of pageNumbers; track pn) {
                      <li class="page-item" [class.active]="pn === page.number">
                        <button class="page-link" (click)="changePage(pn)">{{ pn + 1 }}</button>
                      </li>
                    }
                    <li class="page-item" [class.disabled]="page.last">
                      <button class="page-link" (click)="changePage(page.number + 1)">&raquo;</button>
                    </li>
                    <li class="page-item" [class.disabled]="page.last">
                      <button class="page-link" (click)="changePage(page.totalPages - 1)">&raquo;&raquo;</button>
                    </li>
                  </ul>
                </nav>
              </div>
            }
          }
        </div>
      </div>
    </div>

    @if (showModal) {
      <app-form-modal #formModal [title]="modalTitle" [fields]="formFields" [model]="editModel"
        [submitLabel]="modalSubmitLabel"
        (save)="onModalSave($event)" (cancel)="closeModal()" />
    }

    @if (showQuickModal) {
      <app-form-modal #quickModal [title]="quickTitle" [fields]="quickFormFields" [model]="quickModel"
        submitLabel="Update"
        (save)="onQuickSave($event)" (cancel)="closeQuickModal()" />
    }

    @if (showViewModal) {
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" tabindex="-1" role="dialog" (click)="showViewModal = false">
        <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered" (click)="$event.stopPropagation()">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title fw-bold">{{ viewTitle }}</h5>
              <button type="button" class="btn-close" (click)="showViewModal = false"></button>
            </div>
            <div class="modal-body">
              @for (field of formFields; track field.name) {
                @if (!isExcluded(field.name)) {
                  <div class="mb-2 row g-0">
                    <div class="col-4"><small class="text-muted fw-medium">{{ field.label }}</small></div>
                    <div class="col-8">{{ formatFieldValue(viewModel, field) }}</div>
                  </div>
                  @if (!$last) { <hr class="my-1 opacity-25"> }
                }
              }
            </div>
            <div class="modal-footer">
              <button class="btn btn-sm btn-outline-secondary px-3" (click)="showViewModal = false">Close</button>
            </div>
          </div>
        </div>
      </div>
    }

    @if (showDeleteConfirm) {
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" tabindex="-1" role="dialog" (click)="showDeleteConfirm = false">
        <div class="modal-dialog modal-dialog-centered" (click)="$event.stopPropagation()">
          <div class="modal-content border-danger">
            <div class="modal-header">
              <h5 class="modal-title fw-bold text-danger"><i class="bi bi-exclamation-triangle-fill me-2"></i>Confirm Delete</h5>
              <button type="button" class="btn-close" (click)="showDeleteConfirm = false"></button>
            </div>
            <div class="modal-body">
              <p class="mb-1">Are you sure you want to delete this {{ itemName }}?</p>
              <p class="text-muted small mb-0">This action cannot be undone.</p>
            </div>
            <div class="modal-footer">
              <button class="btn btn-sm btn-outline-secondary px-3" (click)="showDeleteConfirm = false">Cancel</button>
              <button class="btn btn-sm btn-danger px-4" (click)="confirmDelete()" [disabled]="deleting">
                @if (deleting) { <span class="spinner-border spinner-border-sm me-1"></span> }
                Delete
              </button>
            </div>
          </div>
        </div>
      </div>
    }
  `,
  styles: [`
    .modal-backdrop { z-index: 1050; }
    .modal { z-index: 1055; }
    .table th { white-space: nowrap; }
    .btn-group .btn { padding: 0.2rem 0.4rem; }
    .pagination { margin: 0; }
  `]
})
export class CrudListComponent implements OnInit, OnDestroy {
  @ViewChild('formModal') formModalComponent?: FormModalComponent;
  @ViewChild('quickModal') quickModalComponent?: FormModalComponent;

  title = '';
  resource = '';
  columns: ColumnDef[] = [];
  formFields: FormFieldDef[] = [];
  quickFormFields: FormFieldDef[] = [];
  idField = 'id';
  itemName = '';
  titleIcon = 'bi-table';

  items: any[] = [];
  page: PageResponse<any> = { content: [], totalPages: 0, totalElements: 0, size: 20, number: 0, first: true, last: true };
  loading = true;
  errorMessage = '';

  searchTerm = '';
  searchPlaceholder = 'Search...';
  pageSize = 20;

  filters: FilterDef[] = [];
  filterValues: Record<string, any> = {};
  filterOptions: Record<string, { label: string; value: any }[]> = {};
  entityScope: EntityScope = {};

  showModal = false;
  showQuickModal = false;
  showViewModal = false;
  showDeleteConfirm = false;
  isEditing = false;
  editModel: any = {};
  quickModel: any = {};
  quickTitle = '';
  viewModel: any = {};
  modalTitle = '';
  modalSubmitLabel = 'Save';
  viewTitle = '';
  deleting = false;
  selectedItem: any = null;

  private destroy$ = new Subject<void>();

  constructor(private route: ActivatedRoute, private api: ApiService, private toast: ToastService,
              private globalFilter: GlobalFilterService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.globalFilter.state$.pipe(takeUntil(this.destroy$)).subscribe(() => {
      if (this.resource) {
        this.entityScope = getEntityScope(this.resource);
        this.loadItems(0);
        this.loadFilterOptions();
      }
    });

    this.route.data.pipe(takeUntil(this.destroy$)).subscribe(data => {
      this.resource = data['resource'] || this.resource;
      const cfg = getEntityConfig(this.resource);
      this.title = cfg?.title || data['title'] || this.title;
      this.columns = cfg?.columns || data['columns'] || this.columns;
      this.formFields = cfg?.formFields || [];
      this.quickFormFields = cfg?.quickFields || [];
      this.idField = cfg?.idField || data['idField'] || 'id';
      this.itemName = cfg?.itemName || data['itemName'] || 'item';
      this.searchPlaceholder = getEntitySearchPlaceholder(this.resource);
      this.filters = getEntityFilters(this.resource);
      this.entityScope = getEntityScope(this.resource);

      this.items = [];
      this.page = { content: [], totalPages: 0, totalElements: 0, size: this.pageSize, number: 0, first: true, last: true };
      this.loading = true;
      this.errorMessage = '';
      this.searchTerm = '';
      this.filterValues = {};
      this.showModal = false;
      this.showQuickModal = false;
      this.showViewModal = false;
      this.showDeleteConfirm = false;
      this.isEditing = false;
      this.editModel = {};
      this.quickModel = {};
      this.quickTitle = '';
      this.viewModel = {};
      this.selectedItem = null;

      this.loadFilterOptions();
      this.loadItems(0);
      this.cdr.detectChanges();
    });
  }

  loadFilterOptions() {
    const resourceFields = this.filters.filter(f => f.resource);
    if (!resourceFields.length) return;
    resourceFields.forEach(field => {
      const params = this.globalFilter.resourceFilterParams(field.resource!);
      this.api.getAllArray<any>(field.resource!, { size: 200, ...params }).pipe(
        takeUntil(this.destroy$)
      ).subscribe(items => {
        const df = field.displayField || 'name';
        const vf = field.valueField || 'id';
        this.filterOptions[field.name] = items
          .map(item => ({ label: item[df] || item[vf], value: item[vf] }))
          .sort((a, b) => String(a.label).localeCompare(String(b.label)));
        this.cdr.detectChanges();
      });
    });
  }

  loadItems(page: number, appendSearch?: string) {
    this.loading = true;
    this.errorMessage = '';
    const params: any = { page, size: this.pageSize };
    const searchVal = appendSearch !== undefined ? appendSearch : this.searchTerm;
    if (searchVal) params.search = searchVal;
    for (const key of Object.keys(this.filterValues)) {
      const val = this.filterValues[key];
      if (val !== undefined && val !== null && val !== '') {
        params[key] = val;
      }
    }
    const globalParams = this.globalFilter.buildGlobalParams(this.entityScope);
    Object.assign(params, globalParams);
    this.api.getAll<any>(this.resource, params).pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.items = res.content;
        this.page = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onFilterChange(name: string, value: any) {
    if (value === '' || value === undefined || value === null) {
      delete this.filterValues[name];
    } else {
      this.filterValues[name] = value;
    }
    this.loadItems(0);
    this.cdr.detectChanges();
  }

  clearFilters() {
    this.filterValues = {};
    this.loadItems(0);
    this.cdr.detectChanges();
  }

  get hasActiveFilters(): boolean {
    return Object.keys(this.filterValues).some(k => this.filterValues[k] !== '' && this.filterValues[k] != null);
  }

  get hasGlobalFilter(): boolean {
    return this.globalFilter.active && (!!this.entityScope.competition || !!this.entityScope.season);
  }

  get globalFilterLabel(): string {
    return this.globalFilter.label;
  }

  clearGlobalFilter() {
    this.globalFilter.clear();
  }

  changePage(page: number) {
    this.loadItems(page);
    this.cdr.detectChanges();
  }

  search() {
    this.loadItems(0);
    this.cdr.detectChanges();
  }

  clearSearch() {
    this.searchTerm = '';
    this.loadItems(0);
    this.cdr.detectChanges();
  }

  get pageNumbers(): number[] {
    const tp = this.page.totalPages;
    const cp = this.page.number;
    if (tp <= 5) return Array.from({ length: tp }, (_, i) => i);
    const start = Math.max(0, cp - 2);
    const end = Math.min(tp - 1, cp + 2);
    if (end - start < 4) {
      if (start === 0) return Array.from({ length: 5 }, (_, i) => i);
      return Array.from({ length: 5 }, (_, i) => tp - 5 + i);
    }
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }

  resolveValue(item: any, field: string): any {
    return field.split('.').reduce((o, k) => (o && o[k] !== undefined ? o[k] : undefined), item);
  }

  openCreate() {
    this.editModel = {};
    this.isEditing = false;
    this.modalTitle = 'Add ' + this.itemName;
    this.modalSubmitLabel = 'Create';
    this.showModal = true;
    this.cdr.detectChanges();
  }

  isExcluded(name: string) { return EXCLUDED_FORM_FIELDS.includes(name); }

  formatFieldValue(model: any, field: FormFieldDef): string {
    let val = this.resolveValue(model, field.name);
    if ((val === null || val === undefined) && field.resource && field.name.endsWith('Id')) {
      const nested = this.resolveValue(model, field.name.slice(0, -2));
      if (nested && typeof nested === 'object') {
        val = nested[field.displayField || 'name'] ?? nested.id;
      }
    }
    if (val === null || val === undefined) return '-';
    if (field.type === 'boolean') return val ? 'Yes' : 'No';
    if (field.type === 'date') return val;
    if (field.options) {
      const opt = field.options.find(o => o.value === val);
      return opt ? opt.label : val;
    }
    return String(val);
  }

  viewItem(item: any) {
    this.viewModel = { ...item };
    this.viewTitle = 'View ' + this.itemName;
    this.showViewModal = true;
    this.cdr.detectChanges();
  }

  editItem(item: any) {
    this.editModel = { ...item };
    this.isEditing = true;
    this.modalTitle = 'Edit ' + this.itemName;
    this.modalSubmitLabel = 'Update';
    this.showModal = true;
    this.cdr.detectChanges();
  }

  deleteItem(item: any) {
    this.selectedItem = item;
    this.showDeleteConfirm = true;
    this.cdr.detectChanges();
  }

  confirmDelete() {
    if (!this.selectedItem) return;
    this.deleting = true;
    const id = this.selectedItem[this.idField];
    this.api.delete(this.resource, id).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.toast.show(`${this.itemName} deleted successfully`, 'success');
        this.showDeleteConfirm = false;
        this.deleting = false;
        this.selectedItem = null;
        this.loadItems(this.page.number);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.toast.show('Delete failed: ' + err.message, 'danger');
        this.deleting = false;
        this.cdr.detectChanges();
      }
    });
  }

  onModalSave(data: any) {
    const payload = this.buildPayload(data);
    const obs = this.isEditing
      ? this.api.update(this.resource, this.editModel[this.idField], payload)
      : this.api.create(this.resource, payload);
    obs.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        const msg = this.isEditing ? 'updated' : 'created';
        this.toast.show(`${this.itemName} ${msg} successfully`, 'success');
        this.showModal = false;
        this.isEditing = false;
        this.loadItems(this.page.number);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.toast.show('Error: ' + err.message, 'danger');
        this.formModalComponent?.resetSubmitting();
        this.cdr.detectChanges();
      }
    });
  }

  private buildPayload(data: any, fields: FormFieldDef[] = this.formFields): any {
    const payload: any = {};
    for (const field of fields) {
      if (field.name in data && data[field.name] !== undefined && data[field.name] !== null) {
        payload[field.name] = data[field.name];
      }
    }
    return payload;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  closeModal() {
    this.showModal = false;
    this.cdr.detectChanges();
  }

  openQuick(item: any) {
    this.quickModel = { ...item };
    const home = item.homeTeam?.name;
    const away = item.awayTeam?.name;
    this.quickTitle = home && away ? `Quick update · ${home} vs ${away}` : 'Quick update';
    this.showQuickModal = true;
    this.cdr.detectChanges();
  }

  closeQuickModal() {
    this.showQuickModal = false;
    this.cdr.detectChanges();
  }

  onQuickSave(data: any) {
    const payload = this.buildPayload(data, this.quickFormFields);
    this.api.quickUpdate(this.quickModel[this.idField], payload).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.toast.show(`${this.itemName} updated successfully`, 'success');
        this.showQuickModal = false;
        this.loadItems(this.page.number);
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.toast.show('Error: ' + err.message, 'danger');
        this.quickModalComponent?.resetSubmitting();
        this.cdr.detectChanges();
      }
    });
  }
}
