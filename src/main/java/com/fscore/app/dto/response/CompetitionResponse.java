package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompetitionResponse {
    private String id;
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
