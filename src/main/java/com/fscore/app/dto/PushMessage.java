package com.fscore.app.dto;

import lombok.*;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PushMessage {
    private String title;
    private String body;
    private Map<String, String> data;
}
