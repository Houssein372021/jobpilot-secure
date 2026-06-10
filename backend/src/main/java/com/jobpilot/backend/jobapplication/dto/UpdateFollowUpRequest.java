package com.jobpilot.backend.jobapplication.dto;

import java.time.LocalDateTime;

public record UpdateFollowUpRequest(
        LocalDateTime followUpAt) {
}