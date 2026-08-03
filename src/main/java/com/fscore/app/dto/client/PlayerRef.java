package com.fscore.app.dto.client;

public record PlayerRef(
        String id,
        String fullName,
        String firstName,
        String lastName,
        String position,
        String photo,
        String preferredFoot) {
}
