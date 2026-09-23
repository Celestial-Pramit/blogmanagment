package com.blogmanage.blogmanagement.repository;

import com.blogmanage.blogmanagement.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BlogPostRepository extends MongoRepository<BlogPost, String> {

    Optional<BlogPost> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<BlogPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<BlogPost> findByTagsContainingOrderByCreatedAtDesc(String tag, Pageable pageable);
}