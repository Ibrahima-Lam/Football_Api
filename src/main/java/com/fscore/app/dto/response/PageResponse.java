package com.fscore.app.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PageResponse {
    private List content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private boolean first;
    private boolean last;
    
    public static PageResponse fromPage(Page page) {
        return PageResponse.builder()
            .content(page.getContent())
            .totalPages(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .size(page.getSize())
            .number(page.getNumber())
            .first(page.isFirst())
            .last(page.isLast())
            .build();
    }
}
