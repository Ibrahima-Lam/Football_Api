package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamCoachResponse {
    private String id;
    private String teamId;
    private String coachId;
    private String seasonId;
    private String role;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private Boolean interim;
}
