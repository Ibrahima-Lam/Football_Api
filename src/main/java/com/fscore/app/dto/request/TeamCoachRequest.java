package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamCoachRequest {
    private String teamId;
    private String coachId;
    private String seasonId;
    private String role;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private Boolean interim;
}
