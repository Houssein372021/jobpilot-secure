package com.jobpilot.backend.jobapplication.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateJobApplicationRequest(

        @NotBlank(message = "Le nom de l'entreprise est obligatoire") @Size(max = 120, message = "Le nom de l'entreprise ne doit pas dépasser 120 caractères") String companyName,

        @NotBlank(message = "Le titre du poste est obligatoire") @Size(max = 120, message = "Le titre du poste ne doit pas dépasser 120 caractères") String jobTitle,

        @Size(max = 120, message = "La localisation ne doit pas dépasser 120 caractères") String location,

        @Size(max = 50, message = "Le type de contrat ne doit pas dépasser 50 caractères") String contractType,

        @Size(max = 80, message = "La source ne doit pas dépasser 80 caractères") String source,

        @Size(max = 500, message = "L'URL ne doit pas dépasser 500 caractères") String applicationUrl,

        LocalDateTime followUpAt,

        @Size(max = 2000, message = "Les notes ne doivent pas dépasser 2000 caractères") String notes) {
}