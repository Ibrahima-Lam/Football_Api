package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookmakerRequest {
    private String name;
    private String website;
    private String logo;
    private Boolean active;
}
