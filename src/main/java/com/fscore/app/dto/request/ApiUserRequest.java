package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiUserRequest {
    private String email;
    private String name;
    private String plan;
    private Boolean active;
}
