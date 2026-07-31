package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OddResponse {
    private String id;
    private String bookmakerId;
    private String matchId;
    private String market;
    private String selection;
    private java.math.BigDecimal odd;
    private Boolean active;
    private java.time.LocalDateTime recordedAt;
}
