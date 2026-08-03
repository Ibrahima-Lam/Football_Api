package com.fscore.app.dto.client;

public record TeamRef(
        String id,
        String name,
        String shortName,
        String code,
        String logo,
        String kitPrimaryColor,
        String countryIso2,
        String countryFlag) {
}
