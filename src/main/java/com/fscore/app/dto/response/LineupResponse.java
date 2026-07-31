package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LineupResponse {
    private String id;
    private String matchId;
    private String teamId;
    private String playerId;
    private Boolean starter;
    private Boolean captain;
    private Integer shirtNumber;
    private String position;
    private java.math.BigDecimal positionX;
    private java.math.BigDecimal positionY;
    private Integer formationSlot;
}
