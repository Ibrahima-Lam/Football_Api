package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SponsorRequest {
    private String nom;
    private String imageUrl;
    private String description;
    private String websiteUrl;
    private java.math.BigDecimal rating;
}
