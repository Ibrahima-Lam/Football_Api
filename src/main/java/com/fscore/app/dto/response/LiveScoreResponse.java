package com.fscore.app.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LiveScoreResponse {
    private String matchId;
    private String homeTeamId;
    private String awayTeamId;
    private String status;
    private String period;
    private Integer minute;
    private Integer minuteExtra;
    private Integer homeScore;
    private Integer awayScore;
    private Integer homeHtScore;
    private Integer awayHtScore;
    private Integer homeEtScore;
    private Integer awayEtScore;
    private Integer homePenaltyScore;
    private Integer awayPenaltyScore;
    private LocalDateTime updatedAt;
}
