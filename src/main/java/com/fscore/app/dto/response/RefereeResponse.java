package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefereeResponse {
    private String id;
    private String countryId;
    private String firstName;
    private String lastName;
    private String fullName;
    private java.time.LocalDate birthDate;
    private String photo;
    private String category;
    private Boolean active;
}
