package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CityRequest {
    private String countryId;
    private String name;
    private java.math.BigDecimal latitude;
    private java.math.BigDecimal longitude;
    private String timezone;
}
