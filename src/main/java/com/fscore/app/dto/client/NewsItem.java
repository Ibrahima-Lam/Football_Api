package com.fscore.app.dto.client;

import java.time.LocalDateTime;

public record NewsItem(
        String id,
        String title,
        String excerpt,
        String content,
        String image,
        String author,
        LocalDateTime publishedAt,
        String competitionName,
        TeamRef team,
        PlayerRef player) {
}
