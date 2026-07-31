package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HeadToHeadRequest {
    private String team1Id;
    private String team2Id;
    private Integer totalMatches;
    private Integer team1Wins;
    private Integer team2Wins;
    private Integer draws;
    private Integer team1Goals;
    private Integer team2Goals;
}
