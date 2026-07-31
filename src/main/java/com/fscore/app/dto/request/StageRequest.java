package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StageRequest {
    private String seasonId;
    private String name;
    private String type;
    private Integer orderNo;
}
