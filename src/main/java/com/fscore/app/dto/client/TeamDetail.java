package com.fscore.app.dto.client;

public record TeamDetail(
        String id,
        String name,
        String shortName,
        String code,
        Integer founded,
        String logo,
        String kitPrimaryColor,
        String kitSecondaryColor,
        String website,
        String description,
        String countryName,
        String countryIso2,
        String countryFlag,
        String stadiumName,
        String stadiumCity,
        Integer stadiumCapacity) {
}
