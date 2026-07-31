package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TranslationRequest {
    private String entityType;
    private String entityId;
    private String language;
    private String fieldName;
    private String translatedValue;
}
