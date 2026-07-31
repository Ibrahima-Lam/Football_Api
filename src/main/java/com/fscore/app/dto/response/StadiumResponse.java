package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StadiumResponse {
    private String id;
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
