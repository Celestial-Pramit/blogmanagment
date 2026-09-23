package com.blogmanage.blogmanagement.controller.web;

import com.blogmanage.blogmanagement.exception.InvalidCredentialsException;
import com.blogmanage.blogmanagement.exception.InvalidRegistrationException;
import com.blogmanage.blogmanagement.model.User;
import com.blogmanage.blogmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfilePageController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.getByEmail(authentication.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateDisplayName(
            @RequestParam String displayName,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.updateDisplayName(authentication.getName(), displayName);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated.");
        } catch (InvalidRegistrationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.changePassword(authentication.getName(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed.");
        } catch (InvalidCredentialsException | InvalidRegistrationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/profile";
    }
}