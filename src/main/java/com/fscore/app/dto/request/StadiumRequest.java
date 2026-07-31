package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StadiumRequest {
    private String countryId;
    private String cityId;
    private String name;
    private Integer capacity;
    private String surface;
    private java.math.BigDecimal latitude;
    private java.math.BigDecimal longitude;
    private String address;
    private Integer opened;
    private String image;
}
