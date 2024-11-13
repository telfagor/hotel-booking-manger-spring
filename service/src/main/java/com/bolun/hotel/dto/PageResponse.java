package com.bolun.hotel.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageResponse<T> {
    List<T> content;
    Metadata metadata;

    public static <T> PageResponse<T> of (Page<T> page) {
        Metadata meta = new Metadata(page.getNumber() + 1, page.getSize(), page.getTotalElements());
        return new PageResponse<>(page.getContent(), meta);
    }

    @Value
    public static class Metadata {
        int page;
        int size;
        long totalElements;
    }
}
