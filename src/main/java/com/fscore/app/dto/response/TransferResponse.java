package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferResponse {
    private String id;
    private String playerId;
    private String fromTeamId;
    private String toTeamId;
    private java.time.LocalDate transferDate;
    private java.math.BigDecimal fee;
    private String currency;
    private String transferType;
    private String seasonId;
}
