package com.fscore.app.dto.client;

import java.util.List;

public record RefereeItem(
        String id,
        String fullName,
        String photo,
        String category,
        String countryName,
        String countryFlag,
        Long matchesCount,
        List<String> roles) {
}
