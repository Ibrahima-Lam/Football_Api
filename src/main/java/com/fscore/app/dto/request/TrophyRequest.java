package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrophyRequest {
    private String competitionId;
    private String name;
    private String type;
    private String logo;
}
