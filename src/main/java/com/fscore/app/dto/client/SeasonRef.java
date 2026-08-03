package com.fscore.app.dto.client;

import java.time.LocalDate;

public record SeasonRef(
        String id,
        String name,
        Integer yearStart,
        Integer yearEnd,
        LocalDate startDate,
        LocalDate endDate,
        Boolean current,
        String status) {
}
