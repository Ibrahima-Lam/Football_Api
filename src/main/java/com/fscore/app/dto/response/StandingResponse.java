package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StandingResponse {
    private String id;
    private String seasonId;
    private String stageId;
    private String groupId;
    private String teamId;
    private Integer rankPosition;
    private Integer played;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Integer homeWins;
    private Integer homeDraws;
    private Integer homeLosses;
    private Integer awayWins;
    private Integer awayDraws;
    private Integer awayLosses;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer goalDifference;
    private Integer points;
    private String form;
}
