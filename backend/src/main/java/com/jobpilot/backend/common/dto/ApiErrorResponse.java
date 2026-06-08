package com.jobpilot.backend.common.dto;

import java.time.Instant;

public record ApiErrorResponse(
        String message,
        int status,
        String path,
        Instant timestamp
) {
}