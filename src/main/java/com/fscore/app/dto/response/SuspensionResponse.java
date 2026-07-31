package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SuspensionResponse {
    private String id;
    private String playerId;
    private String teamId;
    private String competitionId;
    private String seasonId;
    private String cardType;
    private String reason;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private Integer matchesBanned;
    private Integer matchesRemaining;
    private String status;
}
