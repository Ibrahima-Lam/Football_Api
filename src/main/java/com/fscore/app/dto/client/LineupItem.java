package com.fscore.app.dto.client;

import java.math.BigDecimal;

public record LineupItem(
        TeamRef team,
        PlayerRef player,
        Boolean starter,
        Boolean captain,
        Integer shirtNumber,
        String position,
        BigDecimal positionX,
        BigDecimal positionY,
        Integer formationSlot) {
}
