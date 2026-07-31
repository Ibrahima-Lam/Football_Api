package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchStatisticsTeamRequest {
    private String matchId;
    private String teamId;
    private java.math.BigDecimal possession;
    private Integer shots;
    private Integer shotsOnTarget;
    private Integer shotsOffTarget;
    private Integer shotsBlocked;
    private Integer corners;
    private Integer freeKicks;
    private Integer goalKicks;
    private Integer throwIns;
    private Integer offsides;
    private Integer fouls;
    private Integer yellowCards;
    private Integer yellowRedCards;
    private Integer redCards;
    private Integer passes;
    private Integer passesAccurate;
    private Integer tackles;
    private Integer interceptions;
    private Integer clearances;
    private Integer saves;
    private java.math.BigDecimal xg;
    private java.math.BigDecimal xga;
}
