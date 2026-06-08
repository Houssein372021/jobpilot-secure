package com.jobpilot.backend.common.dto;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        String message,
        int status,
        String path,
        Instant timestamp,
        Map<String, String> errors
) {
}