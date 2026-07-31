package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferRequest {
    private String playerId;
    private String fromTeamId;
    private String toTeamId;
    private java.time.LocalDate transferDate;
    private java.math.BigDecimal fee;
    private String currency;
    private String transferType;
    private String seasonId;
}
