import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { ApiService } from '../services/api.service';
import { ToastService } from '../services/toast.service';

interface ApiKeyItem {
  id: string;
  userId: string;
  name: string;
  maskedKey: string;
  active: boolean;
  expiresAt: string | null;
  lastUsedAt: string | null;
  createdAt: string;
}

@Component({
  selector: 'app-api-keys',
  standalone: true,
  imports: [FormsModule, DatePipe],
  template: `
    <div class="container-fluid py-4">
      <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
        <h2 class="mb-0"><i class="bi bi-key me-1"></i> API Keys</h2>
        <button class="btn btn-primary" (click)="openCreate()" [disabled]="creating">
          <i class="bi bi-plus-lg"></i> New API Key
        </button>
      </div>

      @if (infoMessage) {
        <div class="alert alert-info py-2 small" role="alert">
          <i class="bi bi-info-circle me-1"></i> {{ infoMessage }}
        </div>
      }

      @if (errorMessage) {
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
          <i class="bi bi-exclamation-triangle"></i> {{ errorMessage }}
          <button type="button" class="btn-close" (click)="errorMessage = ''"></button>
        </div>
      }

      <div class="card shadow-sm border-0">
        <div class="card-body">
          @if (loading) {
            <div class="text-center py-5">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
              </div>
            </div>
          } @else if (items.length === 0) {
            <div class="text-center py-5 text-muted">
              <i class="bi bi-key" style="font-size:3rem"></i>
              <p class="mt-2 mb-0">No API keys found.</p>
            </div>
          } @else {
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th>Name</th>
                    <th>Key</th>
                    <th>Status</th>
                    <th>Expires</th>
                    <th>Last used</th>
                    <th>Created</th>
                    <th class="text-end" style="width:120px">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  @for (item of items; track item.id) {
                    <tr [class.table-secondary]="!item.active">
                      <td class="fw-medium">{{ item.name }}</td>
                      <td>
                        <code class="small">{{ item.maskedKey }}</code>
                        <button class="btn btn-sm btn-outline-secondary ms-1" title="Copy key" (click)="copyMasked(item)">
                          <i class="bi bi-clipboard"></i>
                        </button>
                      </td>
                      <td>
                        <span class="badge" [class.bg-success]="item.active" [class.bg-secondary]="!item.active">
                          {{ item.active ? 'Active' : 'Disabled' }}
                        </span>
                        @if (isExpired(item)) {
                          <span class="badge bg-warning text-dark ms-1">Expired</span>
                        }
                      </td>
                      <td class="small">{{ item.expiresAt ? (item.expiresAt | date:'medium') : '-' }}</td>
                      <td class="small">{{ item.lastUsedAt ? (item.lastUsedAt | date:'medium') : 'Never' }}</td>
                      <td class="small">{{ item.createdAt ? (item.createdAt | date:'medium') : '-' }}</td>
                      <td class="text-end">
                        <div class="btn-group btn-group-sm">
                          <button class="btn" [class.btn-outline-success]="!item.active" [class.btn-outline-warning]="item.active"
                            [title]="item.active ? 'Disable' : 'Enable'" (click)="toggle(item)">
                            <i class="bi" [class.bi-toggle-on]="item.active" [class.bi-toggle-off]="!item.active"></i>
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
          }
        </div>
      </div>
    </div>

    @if (showCreateModal) {
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" tabindex="-1" role="dialog" (click)="closeCreate()">
        <div class="modal-dialog modal-dialog-centered" (click)="$event.stopPropagation()">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title fw-bold"><i class="bi bi-plus-circle me-1"></i> New API Key</h5>
              <button type="button" class="btn-close" (click)="closeCreate()"></button>
            </div>
            <div class="modal-body">
              <form (submit)="submitCreate()" novalidate>
                <div class="mb-3">
                  <label class="form-label" for="ak-name">Name <span class="text-danger">*</span></label>
                  <input type="text" id="ak-name" class="form-control" [(ngModel)]="createName" name="name"
                    placeholder="e.g. Mobile app, Data feed" required maxlength="200" autofocus>
                </div>
                <div class="mb-1">
                  <label class="form-label" for="ak-expires">Expires (optional)</label>
                  <input type="datetime-local" id="ak-expires" class="form-control" [(ngModel)]="createExpires" name="expiresAt">
                </div>
                <div class="form-text mb-3">
                  The full key is shown only once after creation. Store it securely.
                </div>
                <div class="d-flex justify-content-end gap-2">
                  <button type="button" class="btn btn-outline-secondary" (click)="closeCreate()">Cancel</button>
                  <button type="submit" class="btn btn-primary" [disabled]="creating">
                    @if (creating) { <span class="spinner-border spinner-border-sm me-1"></span> }
                    Create
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    }

    @if (createdKey) {
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" tabindex="-1" role="dialog" (click)="createdKey = null">
        <div class="modal-dialog modal-dialog-centered" (click)="$event.stopPropagation()">
          <div class="modal-content border-success">
            <div class="modal-header">
              <h5 class="modal-title fw-bold text-success"><i class="bi bi-check-circle me-1"></i> API Key created</h5>
              <button type="button" class="btn-close" (click)="createdKey = null"></button>
            </div>
            <div class="modal-body">
              <p class="small mb-2">Copy this key now. It won't be shown again.</p>
              <div class="input-group">
                <input type="text" class="form-control font-monospace small" [value]="createdKey" readonly>
                <button class="btn btn-outline-secondary" type="button" title="Copy" (click)="copyKey()">
                  <i class="bi bi-clipboard"></i>
                </button>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-sm btn-success px-4" (click)="createdKey = null">Done</button>
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
              <h5 class="modal-title fw-bold text-danger"><i class="bi bi-exclamation-triangle-fill me-2"></i>Revoke API key</h5>
              <button type="button" class="btn-close" (click)="showDeleteConfirm = false"></button>
            </div>
            <div class="modal-body">
              <p class="mb-1">Revoke <strong>{{ selectedItem?.name }}</strong>?</p>
              <p class="text-muted small mb-0">External applications using this key will lose access immediately.</p>
            </div>
            <div class="modal-footer">
              <button class="btn btn-sm btn-outline-secondary px-3" (click)="showDeleteConfirm = false">Cancel</button>
              <button class="btn btn-sm btn-danger px-4" (click)="confirmDelete()" [disabled]="deleting">
                @if (deleting) { <span class="spinner-border spinner-border-sm me-1"></span> }
                Revoke
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
    code { user-select: all; }
  `]
})
export class ApiKeysComponent implements OnInit, OnDestroy {
  items: ApiKeyItem[] = [];
  loading = true;
  errorMessage = '';
  infoMessage = '';

  creating = false;
  showCreateModal = false;
  createName = '';
  createExpires = '';

  createdKey: string | null = null;

  showDeleteConfirm = false;
  deleting = false;
  selectedItem: ApiKeyItem | null = null;

  private destroy$ = new Subject<void>();

  constructor(private api: ApiService, private toast: ToastService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadItems();
  }

  loadItems() {
    this.loading = true;
    this.errorMessage = '';
    this.api.getAll<ApiKeyItem>('api-keys', { size: 100 }).pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.items = res.content;
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

  openCreate() {
    this.createName = '';
    this.createExpires = '';
    this.showCreateModal = true;
    this.cdr.detectChanges();
  }

  closeCreate() {
    if (this.creating) return;
    this.showCreateModal = false;
    this.cdr.detectChanges();
  }

  submitCreate() {
    if (this.creating || !this.createName.trim()) return;
    this.creating = true;
    const payload: any = { name: this.createName.trim() };
    if (this.createExpires) {
      payload.expiresAt = this.createExpires.length === 16 ? this.createExpires + ':00' : this.createExpires;
    }
    this.api.create<any>('api-keys', payload).pipe(takeUntil(this.destroy$)).subscribe({
      next: (res) => {
        this.creating = false;
        this.showCreateModal = false;
        this.createdKey = res.key;
        this.infoMessage = 'Key "' + res.name + '" created. It will expire' + (res.expiresAt ? ' on ' + new Date(res.expiresAt).toLocaleString() : ' never') + '.';
        this.loadItems();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.creating = false;
        this.toast.show('Error: ' + err.message, 'danger');
        this.cdr.detectChanges();
      }
    });
  }

  copyKey() {
    if (!this.createdKey) return;
    navigator.clipboard.writeText(this.createdKey).then(() => {
      this.toast.show('Key copied to clipboard', 'success');
      this.cdr.detectChanges();
    }).catch(() => {
      this.toast.show('Copy failed', 'danger');
      this.cdr.detectChanges();
    });
  }

  copyMasked(item: ApiKeyItem) {
    navigator.clipboard.writeText(item.maskedKey).then(() => {
      this.toast.show('Key copied to clipboard', 'success');
      this.cdr.detectChanges();
    }).catch(() => {
      this.toast.show('Copy failed', 'danger');
      this.cdr.detectChanges();
    });
  }

  toggle(item: ApiKeyItem) {
    this.api.update<ApiKeyItem>('api-keys', item.id, { name: item.name, active: !item.active }).pipe(
      takeUntil(this.destroy$)
    ).subscribe({
      next: () => {
        this.toast.show('Key "' + item.name + '" ' + (item.active ? 'disabled' : 'enabled'), 'success');
        this.loadItems();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.toast.show('Error: ' + err.message, 'danger');
        this.cdr.detectChanges();
      }
    });
  }

  deleteItem(item: ApiKeyItem) {
    this.selectedItem = item;
    this.showDeleteConfirm = true;
    this.cdr.detectChanges();
  }

  confirmDelete() {
    if (!this.selectedItem) return;
    this.deleting = true;
    this.api.delete('api-keys', this.selectedItem.id).pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.toast.show('Key "' + this.selectedItem!.name + '" revoked', 'success');
        this.showDeleteConfirm = false;
        this.deleting = false;
        this.selectedItem = null;
        this.loadItems();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.toast.show('Error: ' + err.message, 'danger');
        this.deleting = false;
        this.cdr.detectChanges();
      }
    });
  }

  isExpired(item: ApiKeyItem): boolean {
    return !!item.expiresAt && new Date(item.expiresAt).getTime() < Date.now();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
