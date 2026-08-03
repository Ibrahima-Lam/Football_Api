package com.fscore.app.dto.client;

import java.time.LocalDate;

public record CoachItem(
        String id,
        String fullName,
        String photo,
        String role,
        TeamRef team,
        LocalDate startDate,
        LocalDate endDate,
        Boolean interim) {
}
