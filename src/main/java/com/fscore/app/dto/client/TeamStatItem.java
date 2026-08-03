package com.fscore.app.dto.client;

import java.math.BigDecimal;

public record TeamStatItem(
        TeamRef team,
        BigDecimal possession,
        Integer shots,
        Integer shotsOnTarget,
        Integer shotsOffTarget,
        Integer shotsBlocked,
        Integer corners,
        Integer freeKicks,
        Integer goalKicks,
        Integer throwIns,
        Integer offsides,
        Integer fouls,
        Integer yellowCards,
        Integer yellowRedCards,
        Integer redCards,
        Integer passes,
        Integer passesAccurate,
        Integer tackles,
        Integer interceptions,
        Integer clearances,
        Integer saves,
        BigDecimal xg,
        BigDecimal xga) {
}
