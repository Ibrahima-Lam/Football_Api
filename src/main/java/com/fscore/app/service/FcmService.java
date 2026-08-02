package com.fscore.app.service;

import com.fscore.app.dto.PushMessage;
import com.fscore.app.entity.DeviceToken;
import com.fscore.app.repository.DeviceTokenRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FcmService {

    private static final Logger log = LoggerFactory.getLogger(FcmService.class);
    private static final int BATCH_SIZE = 500;

    private final ObjectProvider<FirebaseApp> firebaseAppProvider;
    private final DeviceTokenRepository deviceTokenRepository;

    public FcmService(ObjectProvider<FirebaseApp> firebaseAppProvider,
                      DeviceTokenRepository deviceTokenRepository) {
        this.firebaseAppProvider = firebaseAppProvider;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    @Async("fcmExecutor")
    public void sendToToken(String token, PushMessage message) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        FirebaseApp app = firebaseAppProvider.getIfAvailable();
        if (app == null) {
            return;
        }
        try {
            Message msg = Message.builder()
                .setToken(token)
                .setNotification(toNotification(message))
                .putAllData(normalizeData(message.getData()))
                .build();
            FirebaseMessaging.getInstance(app).send(msg);
        } catch (FirebaseMessagingException e) {
            log.warn("FCM send to token failed: {}", e.getMessage());
            if (isInvalidToken(e)) {
                removeStoredToken(token);
            }
        }
    }

    @Async("fcmExecutor")
    public void sendToTokens(List<String> tokens, PushMessage message) {
        doSendToTokens(tokens, message);
    }

    @Async("fcmExecutor")
    public void sendToAllTokens(PushMessage message) {
        List<String> tokens = deviceTokenRepository.findAll().stream()
            .map(DeviceToken::getToken)
            .filter(StringUtils::hasText)
            .toList();
        doSendToTokens(tokens, message);
    }

    private void doSendToTokens(List<String> tokens, PushMessage message) {
        if (tokens == null || tokens.isEmpty()) {
            return;
        }
        FirebaseApp app = firebaseAppProvider.getIfAvailable();
        if (app == null) {
            return;
        }
        try {
            for (List<String> batch : partition(tokens, BATCH_SIZE)) {
                MulticastMessage.Builder builder = MulticastMessage.builder()
                    .setNotification(toNotification(message))
                    .putAllData(normalizeData(message.getData()));
                batch.forEach(builder::addToken);
                BatchResponse response = FirebaseMessaging.getInstance(app).sendEachForMulticast(builder.build());
                if (response.getFailureCount() > 0) {
                    log.warn("FCM multicast: {}/{} messages failed",
                        response.getFailureCount(), batch.size());
                    cleanupInvalidTokens(batch, response.getResponses());
                }
            }
        } catch (FirebaseMessagingException e) {
            log.warn("FCM multicast failed: {}", e.getMessage());
        }
    }

    @Async("fcmExecutor")
    public void sendToTopic(String topic, PushMessage message) {
        if (!StringUtils.hasText(topic)) {
            return;
        }
        FirebaseApp app = firebaseAppProvider.getIfAvailable();
        if (app == null) {
            return;
        }
        try {
            Message msg = Message.builder()
                .setTopic(topic)
                .setNotification(toNotification(message))
                .putAllData(normalizeData(message.getData()))
                .build();
            FirebaseMessaging.getInstance(app).send(msg);
        } catch (FirebaseMessagingException e) {
            log.warn("FCM send to topic failed: {}", e.getMessage());
        }
    }

    @Async("fcmExecutor")
    public void subscribeToTopic(String token, String topic) {
        FirebaseApp app = firebaseAppProvider.getIfAvailable();
        if (app == null || !StringUtils.hasText(token) || !StringUtils.hasText(topic)) {
            return;
        }
        try {
            FirebaseMessaging.getInstance(app).subscribeToTopic(List.of(token), topic);
        } catch (FirebaseMessagingException e) {
            log.warn("FCM subscribe to topic failed: {}", e.getMessage());
        }
    }

    private Notification toNotification(PushMessage message) {
        if (!StringUtils.hasText(message.getTitle())) {
            return null;
        }
        return Notification.builder()
            .setTitle(message.getTitle())
            .setBody(message.getBody())
            .build();
    }

    private Map<String, String> normalizeData(Map<String, String> data) {
        return data != null ? data : Map.of();
    }

    private void cleanupInvalidTokens(List<String> tokens, List<SendResponse> responses) {
        for (int i = 0; i < responses.size() && i < tokens.size(); i++) {
            SendResponse response = responses.get(i);
            if (response.isSuccessful()) {
                continue;
            }
            Exception exception = response.getException();
            if (exception instanceof FirebaseMessagingException fme && isInvalidToken(fme)) {
                removeStoredToken(tokens.get(i));
            }
        }
    }

    private boolean isInvalidToken(FirebaseMessagingException exception) {
        MessagingErrorCode code = exception.getMessagingErrorCode();
        return code == MessagingErrorCode.UNREGISTERED || code == MessagingErrorCode.INVALID_ARGUMENT;
    }

    private void removeStoredToken(String token) {
        deviceTokenRepository.findByToken(token).ifPresent(deviceTokenRepository::delete);
    }

    private List<List<String>> partition(List<String> tokens, int size) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i += size) {
            batches.add(tokens.subList(i, Math.min(i + size, tokens.size())));
        }
        return batches;
    }
}
