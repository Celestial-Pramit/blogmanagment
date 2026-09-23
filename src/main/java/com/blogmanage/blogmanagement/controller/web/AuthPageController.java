package com.blogmanage.blogmanagement.controller.web;

import com.blogmanage.blogmanagement.dto.RegisterRequest;
import com.blogmanage.blogmanagement.exception.DuplicateUserException;
import com.blogmanage.blogmanagement.exception.InvalidRegistrationException;
import com.blogmanage.blogmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthPageController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String displayName,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.register(new RegisterRequest(email, password, displayName));
        } catch (InvalidRegistrationException | DuplicateUserException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/auth/register";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Account created. Please sign in.");
        return "redirect:/auth/login";
    }
}