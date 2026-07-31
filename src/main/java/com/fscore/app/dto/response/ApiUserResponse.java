package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiUserResponse {
    private String id;
    private String email;
    private String name;
    private String plan;
    private Boolean active;
}
