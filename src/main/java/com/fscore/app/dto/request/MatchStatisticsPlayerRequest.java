package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchStatisticsPlayerRequest {
    private String matchId;
    private String teamId;
    private String playerId;
    private Integer minutesPlayed;
    private Integer goals;
    private Integer assists;
    private Integer shots;
    private Integer shotsOnTarget;
    private java.math.BigDecimal xg;
    private Integer keyPasses;
    private Integer passes;
    private Integer passesAccurate;
    private Integer longBalls;
    private Integer crosses;
    private Integer dribblesAttempted;
    private Integer dribblesSucceeded;
    private Integer tackles;
    private Integer interceptions;
    private Integer clearances;
    private Integer foulsCommitted;
    private Integer foulsDrawn;
    private Integer yellowCards;
    private Integer redCards;
    private Integer saves;
    private Integer goalsConceded;
    private java.math.BigDecimal rating;
}
