package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamTrophyResponse {
    private String id;
    private String teamId;
    private String trophyId;
    private String seasonId;
}
