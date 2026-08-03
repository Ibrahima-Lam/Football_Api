package com.fscore.app.dto.client;

public record MatchEventItem(
        String id,
        Integer minute,
        Integer extraMinute,
        String period,
        TeamRef team,
        PlayerRef player,
        PlayerRef relatedPlayer,
        String eventType,
        String detail,
        String comments,
        Boolean varReviewed) {
}
