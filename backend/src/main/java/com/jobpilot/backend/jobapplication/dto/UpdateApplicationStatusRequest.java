package com.jobpilot.backend.jobapplication.dto;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusRequest(

        @NotNull(message = "Le statut est obligatoire") ApplicationStatus status) {
}