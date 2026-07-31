package com.fscore.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiKeyRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String userId;
    private Boolean active;
    private LocalDateTime expiresAt;
}
