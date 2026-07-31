package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CountryRequest {
    private String continentId;
    private String name;
    private String officialName;
    private String iso2;
    private String iso3;
    private String fifaCode;
    private String flagUrl;
}
