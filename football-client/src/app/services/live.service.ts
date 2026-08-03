import { Injectable, signal } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Client, StompSubscription } from '@stomp/stompjs';
import { SettingsService } from './settings.service';

function defaultBrokerUrl(): string {
  const proto = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${proto}//${window.location.host}/ws`;
}

@Injectable({ providedIn: 'root' })
export class LiveService {
  private client: Client | null = null;
  private topics = new Map<string, Subject<unknown>>();
  private subs = new Map<string, StompSubscription>();

  readonly connected = signal(false);

  constructor(private settings: SettingsService) {}

  ensureConnected(): void {
    if (this.client?.connected) return;
    if (this.client) {
      try {
        this.client.deactivate();
      } catch {
        /* ignore */
      }
    }
    const brokerUrl = this.settings.brokerUrl() || defaultBrokerUrl();
    this.client = new Client({
      brokerURL: brokerUrl,
      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      onConnect: () => {
        this.connected.set(true);
        this.resubscribeAll();
      },
      onWebSocketClose: () => this.connected.set(false),
      onWebSocketError: () => this.connected.set(false),
    });
    this.client.activate();
  }

  disconnect(): void {
    if (this.client) {
      try {
        this.client.deactivate();
      } catch {
        /* ignore */
      }
      this.client = null;
    }
    this.connected.set(false);
  }

  subscribe<T>(topic: string): Observable<T> {
    const subject = this.topicSubject(topic);
    this.ensureConnected();
    this.addSubscription(topic);
    return subject.asObservable() as Observable<T>;
  }

  private topicSubject(topic: string): Subject<unknown> {
    let subject = this.topics.get(topic);
    if (!subject) {
      subject = new Subject<unknown>();
      this.topics.set(topic, subject);
    }
    return subject;
  }

  private addSubscription(topic: string): void {
    if (!this.client?.connected || this.subs.has(topic)) return;
    const sub = this.client.subscribe(topic, (message) => {
      try {
        this.topicSubject(topic).next(JSON.parse(message.body));
      } catch {
        /* ignore malformed payload */
      }
    });
    this.subs.set(topic, sub);
  }

  private resubscribeAll(): void {
    this.subs.clear();
    for (const topic of this.topics.keys()) {
      this.addSubscription(topic);
    }
  }
}
