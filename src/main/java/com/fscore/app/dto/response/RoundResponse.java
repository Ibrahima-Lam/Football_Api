package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoundResponse {
    private String id;
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
