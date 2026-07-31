package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RateLimitRequest {
    private String apiKeyId;
    private Integer requestsPerMinute;
    private Integer requestsPerDay;
    private Integer requestsPerMonth;
}
