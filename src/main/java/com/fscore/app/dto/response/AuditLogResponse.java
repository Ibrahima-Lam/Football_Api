package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLogResponse {
    private String id;
    private String apiKeyId;
    private String endpoint;
    private String method;
    private Integer statusCode;
    private Integer responseTimeMs;
    private String ipAddress;
}
