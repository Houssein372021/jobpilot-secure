package com.jobpilot.backend.auth.service;

import com.jobpilot.backend.auth.dto.AuthResponse;
import com.jobpilot.backend.auth.dto.RegisterRequest;
import com.jobpilot.backend.user.entity.User;
import com.jobpilot.backend.user.entity.UserRole;
import com.jobpilot.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        User user = new User(
                UUID.randomUUID(),
                request.email().toLowerCase(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserRole.USER,
                true,
                LocalDateTime.now(),
                null
        );

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole().name()
        );
    }
}