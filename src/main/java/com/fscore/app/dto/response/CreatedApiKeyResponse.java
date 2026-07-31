package com.fscore.app.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatedApiKeyResponse {
    private String id;
    private String userId;
    private String name;
    private String key;
    private LocalDateTime expiresAt;
}
