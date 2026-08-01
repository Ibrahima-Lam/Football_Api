package com.fscore.app.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceTokenResponse {
    private String id;
    private String userId;
    private String token;
    private String platform;
    private LocalDateTime createdAt;
}
