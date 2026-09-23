package com.blogmanage.blogmanagement.dto;

import com.blogmanage.blogmanagement.model.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BlogUpdateRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        String title,

        @Size(max = 500, message = "Excerpt must be at most 500 characters")
        String excerpt,

        @NotBlank(message = "Content is required")
        String content,

        String coverImageUrl,

        List<String> tags,

        Visibility visibility
) {
}