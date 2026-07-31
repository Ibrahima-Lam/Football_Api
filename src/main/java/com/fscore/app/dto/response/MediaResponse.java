package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MediaResponse {
    private String id;
    private String entityType;
    private String entityId;
    private String mediaType;
    private String title;
    private String description;
    private String url;
    private String thumbnailUrl;
    private Integer duration;
    private String language;
    private java.time.LocalDateTime publishedAt;
}
