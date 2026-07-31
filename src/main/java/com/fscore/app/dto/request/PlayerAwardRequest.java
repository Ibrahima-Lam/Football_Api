package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerAwardRequest {
    private String playerId;
    private String trophyId;
    private String seasonId;
    private String teamId;
}
