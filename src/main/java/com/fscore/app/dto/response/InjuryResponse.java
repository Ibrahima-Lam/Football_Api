package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InjuryResponse {
    private String id;
    private String playerId;
    private String teamId;
    private String matchId;
    private String injuryType;
    private String bodyPart;
    private String severity;
    private java.time.LocalDate startDate;
    private java.time.LocalDate expectedReturn;
    private java.time.LocalDate actualReturn;
    private String status;
}
