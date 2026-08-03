package com.fscore.app.dto.client;

public record CompetitionRef(
        String id,
        String name,
        String shortName,
        String type,
        String gender,
        String ageLevel,
        String sport,
        String logo,
        Integer level,
        String countryName,
        String countryIso2,
        String countryFlag,
        String confederationName,
        String confederationAcronym,
        SeasonRef currentSeason) {
}
