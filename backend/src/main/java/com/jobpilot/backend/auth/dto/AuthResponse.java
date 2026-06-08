package com.jobpilot.backend.auth.dto;

import java.util.UUID;

public record AuthResponse(
        String token,
        String tokenType,
        UUID userId,
        String email,
        String firstName,
        String lastName,
        String role
) {
}