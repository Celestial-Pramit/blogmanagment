package com.blogmanage.blogmanagement.service;

import com.blogmanage.blogmanagement.dto.RegisterRequest;
import com.blogmanage.blogmanagement.exception.DuplicateUserException;
import com.blogmanage.blogmanagement.exception.InvalidCredentialsException;
import com.blogmanage.blogmanagement.exception.InvalidRegistrationException;
import com.blogmanage.blogmanagement.exception.ResourceNotFoundException;
import com.blogmanage.blogmanagement.model.User;
import com.blogmanage.blogmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(normalize(email))
                .orElseThrow(() -> new UsernameNotFoundException("No account found for " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .roles("USER")
                .build();
    }

    public User register(RegisterRequest request) {
        String email = request.email() == null ? "" : normalize(request.email());
        String password = request.password() == null ? "" : request.password();
        String displayName = request.displayName() == null ? "" : request.displayName().trim();

        if (email.isBlank() || !email.contains("@")) {
            throw new InvalidRegistrationException("A valid email is required.");
        }
        if (password.length() < 6) {
            throw new InvalidRegistrationException("Password must be at least 6 characters.");
        }
        if (displayName.isBlank()) {
            throw new InvalidRegistrationException("Display name is required.");
        }
        if (repository.existsByEmail(email)) {
            throw new DuplicateUserException("An account with this email already exists.");
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .displayName(displayName)
                .createdAt(Instant.now())
                .build();

        return repository.save(user);
    }

    public User getByEmail(String email) {
        return repository.findByEmail(normalize(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }

    public User updateDisplayName(String email, String newDisplayName) {
        User user = getByEmail(email);
        String trimmed = newDisplayName == null ? "" : newDisplayName.trim();
        if (trimmed.isBlank()) {
            throw new InvalidRegistrationException("Display name is required.");
        }
        user.setDisplayName(trimmed);
        return repository.save(user);
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = getByEmail(email);
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new InvalidRegistrationException("New password must be at least 6 characters.");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    private String normalize(String email) {
        return email.toLowerCase(Locale.ROOT).trim();
    }
}