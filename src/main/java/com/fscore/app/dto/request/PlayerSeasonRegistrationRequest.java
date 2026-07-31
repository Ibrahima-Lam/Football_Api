package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerSeasonRegistrationRequest {
    private String playerId;
    private String teamId;
    private String seasonId;
    private String competitionId;
    private Integer shirtNumber;
    private String position;
    private String status;
    private java.time.LocalDate registeredAt;
    private java.time.LocalDate unregisteredAt;
    private Boolean captain;
}
