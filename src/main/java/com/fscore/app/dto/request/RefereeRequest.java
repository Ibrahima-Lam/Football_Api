package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefereeRequest {
    private String countryId;
    private String firstName;
    private String lastName;
    private String fullName;
    private java.time.LocalDate birthDate;
    private String photo;
    private String category;
    private Boolean active;
}
