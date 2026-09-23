package com.blogmanage.blogmanagement.service;

import com.blogmanage.blogmanagement.dto.BlogCreateRequest;
import com.blogmanage.blogmanagement.dto.BlogDetailResponse;
import com.blogmanage.blogmanagement.dto.BlogSummaryResponse;
import com.blogmanage.blogmanagement.dto.BlogUpdateRequest;
import com.blogmanage.blogmanagement.dto.PageResponse;
import com.blogmanage.blogmanagement.exception.ForbiddenActionException;
import com.blogmanage.blogmanagement.exception.ResourceNotFoundException;
import com.blogmanage.blogmanagement.model.BlogPost;
import com.blogmanage.blogmanagement.model.Visibility;
import com.blogmanage.blogmanagement.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository repository;

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");

    public PageResponse<BlogSummaryResponse> getFeed(Pageable pageable, String tagFilter) {
        Page<BlogPost> page = (tagFilter == null || tagFilter.isBlank())
                ? repository.findAllByOrderByCreatedAtDesc(pageable)
                : repository.findByTagsContainingOrderByCreatedAtDesc(tagFilter, pageable);

        List<BlogSummaryResponse> items = page.getContent().stream()
                .map(this::toSummary)
                .toList();

        return new PageResponse<>(items, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    public BlogDetailResponse getBySlug(String slug) {
        BlogPost post = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + slug));
        return toDetail(post);
    }

    public BlogDetailResponse getById(String id) {
        BlogPost post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));
        return toDetail(post);
    }

    public BlogDetailResponse create(BlogCreateRequest request, String authorId, String authorName) {
        Instant now = Instant.now();
        String slug = generateUniqueSlug(request.title(), null);

        BlogPost post = BlogPost.builder()
                .title(request.title().trim())
                .slug(slug)
                .excerpt(normalizeExcerpt(request.excerpt(), request.content()))
                .content(request.content().trim())
                .coverImageUrl(request.coverImageUrl())
                .tags(request.tags() == null ? List.of() : request.tags())
                .authorId(authorId)
                .authorName(authorName)
                .createdAt(now)
                .updatedAt(now)
                .visibility(request.visibility() == null ? Visibility.PUBLIC : request.visibility())
                .build();

        return toDetail(repository.save(post));
    }

    public BlogDetailResponse update(String id, BlogUpdateRequest request, String currentUserId) {
        BlogPost post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new ForbiddenActionException("You can only edit your own posts.");
        }

        boolean titleChanged = !post.getTitle().equals(request.title());

        post.setTitle(request.title().trim());
        post.setExcerpt(normalizeExcerpt(request.excerpt(), request.content()));
        post.setContent(request.content().trim());
        post.setCoverImageUrl(request.coverImageUrl());
        post.setTags(request.tags() == null ? List.of() : request.tags());
        post.setUpdatedAt(Instant.now());
        if (request.visibility() != null) {
            post.setVisibility(request.visibility());
        }
        if (titleChanged) {
            post.setSlug(generateUniqueSlug(request.title(), id));
        }

        return toDetail(repository.save(post));
    }

    public void delete(String id, String currentUserId) {
        BlogPost post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found: " + id));

        if (!post.getAuthorId().equals(currentUserId)) {
            throw new ForbiddenActionException("You can only delete your own posts.");
        }

        repository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────

    private String generateUniqueSlug(String title, String excludingId) {
        String base = NON_ALPHANUMERIC
                .matcher(title.toLowerCase(Locale.ROOT).trim())
                .replaceAll("-");
        base = base.replaceAll("^-+|-+$", "");
        if (base.isBlank()) {
            base = "post";
        }

        String candidate = base;
        int suffix = 2;
        while (slugTakenByAnotherPost(candidate, excludingId)) {
            candidate = base + "-" + suffix;
            suffix++;
        }
        return candidate;
    }

    private boolean slugTakenByAnotherPost(String slug, String excludingId) {
        return repository.findBySlug(slug)
                .map(existing -> excludingId == null || !existing.getId().equals(excludingId))
                .orElse(false);
    }

    private String normalizeExcerpt(String excerpt, String content) {
        if (excerpt != null && !excerpt.isBlank()) {
            return excerpt.trim();
        }
        String plain = content == null ? "" : content.trim();
        return plain.length() > 200 ? plain.substring(0, 200) + "..." : plain;
    }

    private BlogSummaryResponse toSummary(BlogPost post) {
        return new BlogSummaryResponse(
                post.getId(), post.getSlug(), post.getTitle(), post.getExcerpt(),
                post.getCoverImageUrl(), post.getTags(), post.getAuthorId(), post.getAuthorName(),
                post.getCreatedAt(), post.getVisibility()
        );
    }

    private BlogDetailResponse toDetail(BlogPost post) {
        return new BlogDetailResponse(
                post.getId(), post.getSlug(), post.getTitle(), post.getExcerpt(), post.getContent(),
                post.getCoverImageUrl(), post.getTags(), post.getAuthorId(), post.getAuthorName(),
                post.getCreatedAt(), post.getUpdatedAt(), post.getVisibility()
        );
    }
}