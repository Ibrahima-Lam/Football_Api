package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CityResponse {
    private String id;
    private String countryId;
    private String name;
    private java.math.BigDecimal latitude;
    private java.math.BigDecimal longitude;
    private String timezone;
}
