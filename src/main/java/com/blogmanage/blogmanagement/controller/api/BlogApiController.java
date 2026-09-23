package com.blogmanage.blogmanagement.controller.api;

import com.blogmanage.blogmanagement.dto.*;
import com.blogmanage.blogmanagement.exception.UnauthorizedAccessException;
import com.blogmanage.blogmanagement.model.User;
import com.blogmanage.blogmanagement.model.Visibility;
import com.blogmanage.blogmanagement.service.BlogPostService;
import com.blogmanage.blogmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogPostService service;
    private final UserService userService;

    @GetMapping
    public PageResponse<BlogSummaryResponse> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String tag
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return service.getFeed(pageable, tag);
    }

    @GetMapping("/{slug}")
    public BlogDetailResponse getBySlug(@PathVariable String slug, Authentication authentication) {
        BlogDetailResponse post = service.getBySlug(slug);
        if (post.visibility() == Visibility.PRIVATE && !isAuthenticated(authentication)) {
            throw new UnauthorizedAccessException("Please sign in to view this post.");
        }
        return post;
    }

    @PostMapping
    public ResponseEntity<BlogDetailResponse> create(
            @Valid @RequestBody BlogCreateRequest request,
            Authentication authentication
    ) {
        User currentUser = userService.getByEmail(authentication.getName());
        BlogDetailResponse created = service.create(request, currentUser.getId(), currentUser.getDisplayName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDetailResponse> update(
            @PathVariable String id,
            @Valid @RequestBody BlogUpdateRequest request,
            Authentication authentication
    ) {
        User currentUser = userService.getByEmail(authentication.getName());
        return ResponseEntity.ok(service.update(id, request, currentUser.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        User currentUser = userService.getByEmail(authentication.getName());
        service.delete(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}