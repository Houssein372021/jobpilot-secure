package com.jobpilot.backend.auth.dto;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        String role
) {
}