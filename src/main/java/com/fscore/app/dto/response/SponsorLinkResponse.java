package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SponsorLinkResponse {
    private String id;
    private String sponsorId;
    private String entityType;
    private String entityId;
    private java.time.LocalDate dateDebut;
    private java.time.LocalDate dateFin;
}
