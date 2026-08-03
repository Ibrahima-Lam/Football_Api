package com.fscore.app.dto.client;

public record SquadPlayerItem(
        String id,
        String fullName,
        String firstName,
        String lastName,
        String position,
        String photo,
        String preferredFoot,
        Integer shirtNumber,
        Boolean captain,
        String status,
        String nationalityName,
        String nationalityFlag) {
}
