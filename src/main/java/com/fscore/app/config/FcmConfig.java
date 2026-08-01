package com.fscore.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "app.fcm.enabled", havingValue = "true")
public class FcmConfig {

    private static final Logger log = LoggerFactory.getLogger(FcmConfig.class);

    private final FcmProperties properties;

    public FcmConfig(FcmProperties properties) {
        this.properties = properties;
    }

    @Bean(destroyMethod = "delete")
    public FirebaseApp firebaseApp() {
        FirebaseApp app = FirebaseApp.getApps().stream()
            .filter(candidate -> candidate.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
            .findFirst()
            .orElseGet(() -> FirebaseApp.initializeApp(buildOptions()));
        log.info("FirebaseApp initialized for FCM notifications");
        return app;
    }

    private FirebaseOptions buildOptions() {
        try {
            GoogleCredentials credentials = loadCredentials();
            FirebaseOptions.Builder builder = FirebaseOptions.builder()
                .setCredentials(credentials);
            if (StringUtils.hasText(properties.getProjectId())) {
                builder.setProjectId(properties.getProjectId());
            }
            return builder.build();
        } catch (IOException e) {
            throw new IllegalStateException("FCM: unable to load credentials", e);
        }
    }

    private GoogleCredentials loadCredentials() throws IOException {
        String path = properties.getServiceAccountPath();
        if (StringUtils.hasText(path)) {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                stream = new FileInputStream(path);
            }
            return GoogleCredentials.fromStream(stream);
        }
        return GoogleCredentials.getApplicationDefault();
    }
}
