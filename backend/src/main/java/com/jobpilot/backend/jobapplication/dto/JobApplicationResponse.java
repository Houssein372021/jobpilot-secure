package com.jobpilot.backend.jobapplication.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobApplicationResponse(
                UUID id,
                String companyName,
                String jobTitle,
                String location,
                String contractType,
                String status,
                String source,
                String applicationUrl,
                String notes,
                LocalDateTime appliedAt,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                boolean favorite) {
}