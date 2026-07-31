package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerAwardResponse {
    private String id;
    private String playerId;
    private String trophyId;
    private String seasonId;
    private String teamId;
}
