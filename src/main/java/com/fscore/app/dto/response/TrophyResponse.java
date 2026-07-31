package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrophyResponse {
    private String id;
    private String competitionId;
    private String name;
    private String type;
    private String logo;
}
