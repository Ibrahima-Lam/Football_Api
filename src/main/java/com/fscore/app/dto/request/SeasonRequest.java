package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeasonRequest {
    private String competitionId;
    private String name;
    private Integer yearStart;
    private Integer yearEnd;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private Boolean current;
    private String status;
}
