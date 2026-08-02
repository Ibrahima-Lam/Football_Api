package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FileResponse {
    private String id;
    private String originalName;
    private String fileName;
    private String category;
    private String contentType;
    private Long size;
    private String url;
    private String createdAt;
}
