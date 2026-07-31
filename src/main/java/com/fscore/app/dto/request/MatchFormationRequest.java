package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchFormationRequest {
    private String matchId;
    private String teamId;
    private String formation;
}
