package com.blogmanage.blogmanagement.dto;

import com.blogmanage.blogmanagement.model.Visibility;

import java.time.Instant;
import java.util.List;

public record BlogSummaryResponse(
        String id,
        String slug,
        String title,
        String excerpt,
        String coverImageUrl,
        List<String> tags,
        String authorId,
        String authorName,
        Instant createdAt,
        Visibility visibility
) {
}