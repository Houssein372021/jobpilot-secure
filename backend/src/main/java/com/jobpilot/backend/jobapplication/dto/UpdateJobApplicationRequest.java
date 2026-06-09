package com.jobpilot.backend.jobapplication.dto;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import jakarta.validation.constraints.Size;

public record UpdateJobApplicationRequest(

        @Size(max = 150, message = "Le nom de l'entreprise ne doit pas dépasser 150 caractères")
        String companyName,

        @Size(max = 150, message = "Le titre du poste ne doit pas dépasser 150 caractères")
        String jobTitle,

        @Size(max = 150, message = "La localisation ne doit pas dépasser 150 caractères")
        String location,

        @Size(max = 50, message = "Le type de contrat ne doit pas dépasser 50 caractères")
        String contractType,

        ApplicationStatus status,

        @Size(max = 100, message = "La source ne doit pas dépasser 100 caractères")
        String source,

        String applicationUrl,

        String notes
) {
}