package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GroupResponse {
    private String id;
    private String stageId;
    private String name;
}
