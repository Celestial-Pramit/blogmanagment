package com.blogmanage.blogmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class BlogPost {

    @Id
    private String id;

    private String title;

    private String slug;

    private String excerpt;

    private String content;

    private String coverImageUrl;

    @Builder.Default
    private List<String> tags = List.of();

    private String authorId;

    private String authorName;

    private Instant createdAt;

    private Instant updatedAt;

    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC;
}