importScripts('https://www.gstatic.com/firebasejs/12.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/12.0.0/firebase-messaging-compat.js');

self.addEventListener('install', (event) => {
  event.waitUntil(
    fetch('/firebase-config.json')
      .then((res) => res.json())
      .then((config) => {
        if (!config.enabled || !config.firebase || !config.firebase.messagingSenderId) {
          return;
        }
        firebase.initializeApp(config.firebase);
        const messaging = firebase.messaging();
        messaging.onBackgroundMessage((payload) => {
          const title = payload.notification?.title || 'Notification';
          const options = {
            body: payload.notification?.body || '',
            icon: '/favicon.ico',
            badge: '/favicon.ico',
            data: payload.data || {}
          };
          self.registration.showNotification(title, options);
        });
      })
      .catch(() => {})
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(self.clients.claim());
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const url = event.notification.data?.url || '/dashboard';
  event.waitUntil(
    self.clients.matchAll({ type: 'window', includeUncontrolled: true }).then((clients) => {
      for (const client of clients) {
        if ('focus' in client) {
          return client.navigate(url).then((c) => c?.focus());
        }
      }
      if (self.clients.openWindow) {
        return self.clients.openWindow(url);
      }
    })
  );
});
