package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchPenaltyShootoutShotResponse {
    private String id;
    private String matchId;
    private String teamId;
    private String playerId;
    private String goalkeeperId;
    private Integer shotOrder;
    private Integer round;
    private String status;
}
