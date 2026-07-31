package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContractResponse {
    private String id;
    private String playerId;
    private String teamId;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private java.math.BigDecimal salary;
    private Integer shirtNumber;
    private Boolean current;
}
