package com.fscore.app.dto.client;

import java.math.BigDecimal;

public record PlayerStatItem(
        PlayerRef player,
        TeamRef team,
        Integer minutesPlayed,
        Integer goals,
        Integer assists,
        Integer shots,
        Integer shotsOnTarget,
        BigDecimal xg,
        Integer keyPasses,
        Integer passes,
        Integer passesAccurate,
        Integer longBalls,
        Integer crosses,
        Integer dribblesAttempted,
        Integer dribblesSucceeded,
        Integer tackles,
        Integer interceptions,
        Integer clearances,
        Integer foulsCommitted,
        Integer foulsDrawn,
        Integer yellowCards,
        Integer redCards,
        Integer saves,
        Integer goalsConceded,
        BigDecimal rating) {
}
