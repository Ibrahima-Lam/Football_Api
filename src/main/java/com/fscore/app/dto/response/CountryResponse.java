package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CountryResponse {
    private String id;
    private String continentId;
    private String name;
    private String officialName;
    private String iso2;
    private String iso3;
    private String fifaCode;
    private String flagUrl;
}
