package com.fscore.app.dto.client;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchEventCreateRequest {
    private Integer minute;
    private Integer extraMinute;
    private String period;
    private String teamId;
    private String playerId;
    private String relatedPlayerId;
    private String eventType;
    private String detail;
    private String comments;
    private Boolean varReviewed;
}
