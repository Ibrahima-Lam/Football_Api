package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TranslationResponse {
    private String id;
    private String entityType;
    private String entityId;
    private String language;
    private String fieldName;
    private String translatedValue;
}
