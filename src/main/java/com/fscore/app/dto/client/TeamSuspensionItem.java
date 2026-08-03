package com.fscore.app.dto.client;

import java.time.LocalDate;

public record TeamSuspensionItem(
        String id,
        PlayerRef player,
        String cardType,
        String reason,
        LocalDate startDate,
        LocalDate endDate,
        Integer matchesBanned,
        Integer matchesRemaining,
        String status,
        String competitionName,
        String seasonName) {
}
