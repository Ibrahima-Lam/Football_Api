package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookmakerResponse {
    private String id;
    private String name;
    private String website;
    private String logo;
    private Boolean active;
}
