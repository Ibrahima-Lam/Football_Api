package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchPenaltyShootoutShotRequest {
    private String matchId;
    private String teamId;
    private String playerId;
    private String goalkeeperId;
    private Integer shotOrder;
    private Integer round;
    private String status;
}
