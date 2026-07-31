package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerRequest {
    private String countryId;
    private String nationalityId;
    private String secondNationalityId;
    private String firstName;
    private String lastName;
    private String fullName;
    private java.time.LocalDate birthDate;
    private String birthPlace;
    private java.math.BigDecimal height;
    private java.math.BigDecimal weight;
    private String preferredFoot;
    private String position;
    private String photo;
    private java.math.BigDecimal marketValue;
    private String status;
    private String twitter;
    private String instagram;
}
