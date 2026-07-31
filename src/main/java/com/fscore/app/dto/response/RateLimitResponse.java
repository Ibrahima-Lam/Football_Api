package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RateLimitResponse {
    private String id;
    private String apiKeyId;
    private Integer requestsPerMinute;
    private Integer requestsPerDay;
    private Integer requestsPerMonth;
}
