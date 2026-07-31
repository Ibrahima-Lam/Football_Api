package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchFormationResponse {
    private String id;
    private String matchId;
    private String teamId;
    private String formation;
}
