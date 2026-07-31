package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MediaRequest {
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
