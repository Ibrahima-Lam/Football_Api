package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLogRequest {
    private String apiKeyId;
    private String endpoint;
    private String method;
    private Integer statusCode;
    private Integer responseTimeMs;
    private String ipAddress;
}
