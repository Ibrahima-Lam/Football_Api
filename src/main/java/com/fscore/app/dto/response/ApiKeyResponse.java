package com.fscore.app.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiKeyResponse {
    private String id;
    private String userId;
    private String name;
    private String maskedKey;
    private Boolean active;
    private LocalDateTime expiresAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime createdAt;
}
