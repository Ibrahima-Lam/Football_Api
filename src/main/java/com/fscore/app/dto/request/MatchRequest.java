package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchRequest {
    private String seasonId;
    private String stageId;
    private String groupId;
    private String roundId;
    private String homeTeamId;
    private String awayTeamId;
    private String stadiumId;
    private String refereeId;
    private java.time.LocalDateTime kickoff;
    private String status;
    private String period;
    private Integer minute;
    private Integer minuteExtra;
    private java.time.LocalDateTime firstHalfStart;
    private java.time.LocalDateTime secondHalfStart;
    private java.time.LocalDateTime extraTimeStart;
    private java.time.LocalDateTime penaltyShootoutStart;
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
    private Integer attendance;
    private String weather;
    private java.math.BigDecimal temperature;
    private java.math.BigDecimal windSpeed;
    private String note;
}
