package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchEventResponse {
    private String id;
    private String matchId;
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
