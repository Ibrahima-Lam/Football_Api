package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompetitionRequest {
    private String countryId;
    private String confederationId;
    private String name;
    private String shortName;
    private String type;
    private String gender;
    private String ageLevel;
    private String sport;
    private Integer level;
    private String logo;
    private Integer founded;
    private String website;
    private Boolean active;
}
