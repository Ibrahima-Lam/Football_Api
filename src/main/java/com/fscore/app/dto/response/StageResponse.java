package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StageResponse {
    private String id;
    private String seasonId;
    private String name;
    private String type;
    private Integer orderNo;
}
