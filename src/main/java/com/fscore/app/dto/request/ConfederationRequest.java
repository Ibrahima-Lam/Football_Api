package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConfederationRequest {
    private String continentId;
    private String name;
    private String acronym;
    private String logo;
    private String website;
    private Integer founded;
    private String headquarters;
}
