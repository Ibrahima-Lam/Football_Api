package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContinentResponse {
    private String id;
    private String code;
    private String name;
}
