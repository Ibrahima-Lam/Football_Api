import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SettingsService {
  private readonly BROKER = 'fc.brokerUrl';
  readonly apiKey = 'fscore_4x78VPZ2YCBY36H9PCEqWQULgT4qm3OFMUAxeYq8uXg';

  readonly brokerUrl = signal<string>(localStorage.getItem(this.BROKER) ?? '');

  setBrokerUrl(url: string): void {
    this.brokerUrl.set(url);
    localStorage.setItem(this.BROKER, url);
  }
}
