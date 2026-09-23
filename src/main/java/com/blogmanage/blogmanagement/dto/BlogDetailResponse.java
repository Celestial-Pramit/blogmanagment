package com.blogmanage.blogmanagement.dto;

import com.blogmanage.blogmanagement.model.Visibility;

import java.time.Instant;
import java.util.List;

public record BlogDetailResponse(
        String id,
        String slug,
        String title,
        String excerpt,
        String content,
        String coverImageUrl,
        List<String> tags,
        String authorId,
        String authorName,
        Instant createdAt,
        Instant updatedAt,
        Visibility visibility
) {
}