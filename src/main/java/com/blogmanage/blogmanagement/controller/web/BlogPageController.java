package com.blogmanage.blogmanagement.controller.web;

import com.blogmanage.blogmanagement.dto.BlogDetailResponse;
import com.blogmanage.blogmanagement.dto.PageResponse;
import com.blogmanage.blogmanagement.dto.BlogSummaryResponse;
import com.blogmanage.blogmanagement.model.User;
import com.blogmanage.blogmanagement.model.Visibility;
import com.blogmanage.blogmanagement.service.BlogPostService;
import com.blogmanage.blogmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BlogPageController {

    private final BlogPostService service;
    private final UserService userService;

    @GetMapping("/")
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String tag,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "createdAt"));
        PageResponse<BlogSummaryResponse> feed = service.getFeed(pageable, tag);
        model.addAttribute("feed", feed);
        model.addAttribute("activeTag", tag);
        return "index";
    }

    @GetMapping("/blogs/{slug}")
    public String detail(@PathVariable String slug, Authentication authentication, Model model) {
        BlogDetailResponse post = service.getBySlug(slug);
        if (post.visibility() == Visibility.PRIVATE && !isAuthenticated(authentication)) {
            return "redirect:/auth/login";
        }
        model.addAttribute("post", post);
        model.addAttribute("contentHtml", post.content().replace("\r\n", "\n").replace("\n", "<br/>"));
        return "blog-detail";
    }

    @GetMapping("/blogs/new")
    public String newForm(Model model) {
        model.addAttribute("mode", "create");
        return "blog-form";
    }

    @GetMapping("/blogs/{id}/edit")
    public String editForm(
            @PathVariable String id,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        BlogDetailResponse post = service.getById(id);
        User currentUser = userService.getByEmail(authentication.getName());

        if (!post.authorId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only edit your own posts.");
            return "redirect:/";
        }

        model.addAttribute("mode", "edit");
        model.addAttribute("post", post);
        return "blog-form";
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}