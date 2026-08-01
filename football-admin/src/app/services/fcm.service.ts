import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ToastService } from './toast.service';

export interface FcmConfig {
  enabled: boolean;
  vapidKey: string;
  firebase: {
    apiKey: string;
    authDomain: string;
    projectId: string;
    storageBucket: string;
    messagingSenderId: string;
    appId: string;
  };
}

export interface FcmState {
  configured: boolean;
  initialized: boolean;
  loading: boolean;
  permission: string;
  token: string | null;
  deviceId: string | null;
  error: string | null;
}

const ENABLED_KEY = 'fscore_fcm_enabled';
const DEVICE_KEY = 'fscore_fcm_device';

@Injectable({
  providedIn: 'root'
})
export class FcmService {
  state = signal<FcmState>({
    configured: false,
    initialized: false,
    loading: false,
    permission: this.supportsNotifications() ? Notification.permission : 'unsupported',
    token: null,
    deviceId: null,
    error: null
  });

  private config: FcmConfig | null = null;
  private messaging: any = null;

  constructor(private http: HttpClient, private toast: ToastService) {}

  async init(): Promise<void> {
    try {
      this.config = await fetch('/firebase-config.json').then(r => r.json());
    } catch {
      this.config = null;
    }
    if (!this.config?.enabled || !this.config.firebase?.messagingSenderId) {
      this.state.update(s => ({ ...s, configured: false, initialized: false }));
      return;
    }
    this.state.update(s => ({ ...s, configured: true }));
    if (!this.supportsNotifications()) {
      this.state.update(s => ({ ...s, permission: 'unsupported' }));
      return;
    }
    this.state.update(s => ({ ...s, permission: Notification.permission }));
    if (localStorage.getItem(ENABLED_KEY) === 'true' && Notification.permission === 'granted') {
      await this.enable();
    }
  }

  async enable(): Promise<void> {
    if (!this.config?.enabled || !this.config.firebase?.messagingSenderId) {
      this.toast.show('FCM non configuré', 'warning');
      return;
    }
    if (!this.supportsNotifications()) {
      this.toast.show('Notifications non supportées par ce navigateur', 'warning');
      return;
    }
    this.state.update(s => ({ ...s, loading: true, error: null }));
    try {
      const permission = await Notification.requestPermission();
      this.state.update(s => ({ ...s, permission }));
      if (permission !== 'granted') {
        this.state.update(s => ({ ...s, loading: false }));
        this.toast.show('Permission de notification refusée', 'warning');
        return;
      }

      const { initializeApp, getApps } = await import('firebase/app');
      const { getMessaging, getToken, onMessage } = await import('firebase/messaging');

      const app = getApps().length ? getApps()[0] : initializeApp(this.config.firebase);
      const messaging = getMessaging(app);
      this.messaging = messaging;

      const registration = await this.registerServiceWorker();
      const token = await getToken(messaging, {
        vapidKey: this.config.vapidKey || undefined,
        serviceWorkerRegistration: registration ?? undefined
      });
      if (!token) {
        throw new Error('Aucun token FCM reçu');
      }

      const res = await firstValueFrom(
        this.http.post<{ id: string }>('/api/device-tokens', { token, platform: 'WEB' })
      );
      localStorage.setItem(ENABLED_KEY, 'true');
      localStorage.setItem(DEVICE_KEY, JSON.stringify({ id: res.id, token }));
      this.state.update(s => ({ ...s, token, deviceId: res.id, loading: false, initialized: true }));

      onMessage(messaging, payload => {
        const title = payload.notification?.title ?? 'Notification';
        const body = payload.notification?.body ?? '';
        this.toast.show(body ? `${title} — ${body}` : title, 'info');
      });
      this.toast.show('Notifications push activées', 'success');
    } catch (e: any) {
      this.state.update(s => ({
        ...s,
        loading: false,
        error: e?.message ?? 'Erreur d\'activation des notifications'
      }));
    }
  }

  async disable(): Promise<void> {
    const saved = localStorage.getItem(DEVICE_KEY);
    if (saved) {
      try {
        const { id } = JSON.parse(saved);
        await firstValueFrom(this.http.delete(`/api/device-tokens/${id}`));
      } catch {
        // token déjà supprimé côté serveur
      }
    }
    if (this.messaging) {
      try {
        const { deleteToken } = await import('firebase/messaging');
        await deleteToken(this.messaging);
      } catch {
        // ignore
      }
    }
    localStorage.removeItem(ENABLED_KEY);
    localStorage.removeItem(DEVICE_KEY);
    this.state.update(s => ({ ...s, token: null, deviceId: null, initialized: false }));
    this.toast.show('Notifications push désactivées', 'info');
  }

  async copyToken(): Promise<void> {
    const token = this.state().token;
    if (!token) return;
    try {
      await navigator.clipboard.writeText(token);
      this.toast.show('Token FCM copié', 'success');
    } catch {
      this.toast.show('Impossible de copier le token', 'danger');
    }
  }

  private async registerServiceWorker(): Promise<ServiceWorkerRegistration | null> {
    if (!('serviceWorker' in navigator)) {
      return null;
    }
    return navigator.serviceWorker.register('/firebase-messaging-sw.js');
  }

  private supportsNotifications(): boolean {
    return typeof window !== 'undefined' && 'Notification' in window;
  }
}
