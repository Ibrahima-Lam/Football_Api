package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OddHistoryResponse {
    private String id;
    private String bookmakerId;
    private String matchId;
    private String market;
    private String selection;
    private java.math.BigDecimal odd;
    private java.time.LocalDateTime recordedAt;
}
