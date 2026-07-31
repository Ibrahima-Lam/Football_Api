package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamTrophyRequest {
    private String teamId;
    private String trophyId;
    private String seasonId;
}
