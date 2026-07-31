package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConfederationResponse {
    private String id;
    private String continentId;
    private String name;
    private String acronym;
    private String logo;
    private String website;
    private Integer founded;
    private String headquarters;
}
