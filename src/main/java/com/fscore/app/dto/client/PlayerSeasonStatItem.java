package com.fscore.app.dto.client;

import java.math.BigDecimal;

public record PlayerSeasonStatItem(
        PlayerRef player,
        TeamRef team,
        Integer appearances,
        Integer appearancesAsStarter,
        Integer minutesPlayed,
        Integer goals,
        Integer assists,
        Integer shots,
        Integer shotsOnTarget,
        Integer yellowCards,
        Integer redCards,
        Integer saves,
        Integer cleanSheets,
        BigDecimal avgRating) {
}
