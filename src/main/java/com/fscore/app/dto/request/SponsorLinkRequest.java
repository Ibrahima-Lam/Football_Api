package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SponsorLinkRequest {
    private String sponsorId;
    private String entityType;
    private String entityId;
    private java.time.LocalDate dateDebut;
    private java.time.LocalDate dateFin;
}
