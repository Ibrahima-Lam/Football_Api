package com.fscore.app.dto.client;

import java.time.LocalDateTime;

public record MatchCard(
        String id,
        LocalDateTime kickoff,
        String status,
        String period,
        Integer minute,
        Integer minuteExtra,
        Integer homeScore,
        Integer awayScore,
        Integer homeHtScore,
        Integer awayHtScore,
        Integer homeEtScore,
        Integer awayEtScore,
        Integer homePenaltyScore,
        Integer awayPenaltyScore,
        String homePenaltyForm,
        String awayPenaltyForm,
        TeamRef homeTeam,
        TeamRef awayTeam,
        CompetitionRef competition,
        SeasonRef season,
        String stageName,
        String groupName,
        String roundName,
        Integer roundNumber,
        String stadiumName) {
}
