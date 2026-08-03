package com.fscore.app.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchLiveUpdateRequest {
    private LocalDateTime kickoff;
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
    private String homePenaltyForm;
    private String awayPenaltyForm;
}
