package com.fscore.app.dto.client;

import java.time.LocalDate;

public record TeamInjuryItem(
        String id,
        PlayerRef player,
        String injuryType,
        String bodyPart,
        String severity,
        LocalDate startDate,
        LocalDate expectedReturn,
        LocalDate actualReturn,
        String status) {
}
