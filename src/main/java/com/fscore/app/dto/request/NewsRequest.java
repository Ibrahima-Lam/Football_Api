package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NewsRequest {
    private String competitionId;
    private String teamId;
    private String playerId;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String image;
    private String author;
    private String language;
    private String sourceUrl;
    private java.time.LocalDateTime publishedAt;
}
