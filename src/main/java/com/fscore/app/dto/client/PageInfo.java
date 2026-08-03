package com.fscore.app.dto.client;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageInfo<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int size,
        int number,
        boolean first,
        boolean last) {

    public static <T> PageInfo<T> from(Page<T> page) {
        return new PageInfo<>(page.getContent(), page.getTotalPages(), page.getTotalElements(),
                page.getSize(), page.getNumber(), page.isFirst(), page.isLast());
    }
}
