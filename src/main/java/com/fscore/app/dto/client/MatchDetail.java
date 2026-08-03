package com.fscore.app.dto.client;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public record MatchDetail(
        MatchCard match,
        String refereeName,
        String stadiumCity,
        Integer attendance,
        String weather,
        BigDecimal temperature,
        BigDecimal windSpeed,
        String note,
        String homePenaltyForm,
        String awayPenaltyForm,
        LocalDateTime firstHalfStart,
        LocalDateTime secondHalfStart,
        LocalDateTime extraTimeStart,
        LocalDateTime penaltyShootoutStart,
        List<MatchEventItem> events,
        List<TeamStatItem> teamStats,
        List<PlayerStatItem> playerStats,
        List<LineupItem> lineups) {
}
