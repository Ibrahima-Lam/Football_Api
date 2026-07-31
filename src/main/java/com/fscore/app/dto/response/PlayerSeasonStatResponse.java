package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerSeasonStatResponse {
    private String id;
    private String playerId;
    private String teamId;
    private String seasonId;
    private String competitionId;
    private Integer appearances;
    private Integer appearancesAsStarter;
    private Integer minutesPlayed;
    private Integer goals;
    private Integer assists;
    private Integer shots;
    private Integer shotsOnTarget;
    private java.math.BigDecimal xg;
    private Integer keyPasses;
    private Integer passes;
    private Integer passesAccurate;
    private Integer dribblesAttempted;
    private Integer dribblesSucceeded;
    private Integer tackles;
    private Integer interceptions;
    private Integer foulsCommitted;
    private Integer foulsDrawn;
    private Integer yellowCards;
    private Integer redCards;
    private Integer saves;
    private Integer goalsConceded;
    private Integer cleanSheets;
    private java.math.BigDecimal avgRating;
}
