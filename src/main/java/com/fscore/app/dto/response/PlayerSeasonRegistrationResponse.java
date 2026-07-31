package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerSeasonRegistrationResponse {
    private String id;
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
