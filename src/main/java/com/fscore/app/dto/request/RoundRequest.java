package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoundRequest {
    private String stageId;
    private String name;
    private String slug;
    private Integer number;
    private String type;
    private Boolean current;
    private String status;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
}
