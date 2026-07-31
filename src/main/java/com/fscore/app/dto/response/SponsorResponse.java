package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SponsorResponse {
    private String id;
    private String nom;
    private String imageUrl;
    private String description;
    private String websiteUrl;
    private java.math.BigDecimal rating;
}
