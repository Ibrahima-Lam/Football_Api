package com.fscore.app.dto.client;

public record StandingItem(
        String id,
        Integer rankPosition,
        TeamRef team,
        Integer played,
        Integer wins,
        Integer draws,
        Integer losses,
        Integer goalsFor,
        Integer goalsAgainst,
        Integer goalDifference,
        Integer points,
        String form,
        String groupId,
        String groupName) {
}
