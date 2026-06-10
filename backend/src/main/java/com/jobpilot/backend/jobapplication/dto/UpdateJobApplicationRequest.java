package com.jobpilot.backend.jobapplication.dto;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import jakarta.validation.constraints.Size;

public record UpdateJobApplicationRequest(

        @Size(max = 120, message = "Le nom de l'entreprise ne doit pas dépasser 120 caractères") String companyName,

        @Size(max = 120, message = "Le titre du poste ne doit pas dépasser 120 caractères") String jobTitle,

        @Size(max = 120, message = "La localisation ne doit pas dépasser 120 caractères") String location,

        @Size(max = 50, message = "Le type de contrat ne doit pas dépasser 50 caractères") String contractType,

        ApplicationStatus status,

        @Size(max = 80, message = "La source ne doit pas dépasser 80 caractères") String source,

        @Size(max = 500, message = "L'URL ne doit pas dépasser 500 caractères") String applicationUrl,

        @Size(max = 2000, message = "Les notes ne doivent pas dépasser 2000 caractères") String notes,

        Boolean favorite) {
}