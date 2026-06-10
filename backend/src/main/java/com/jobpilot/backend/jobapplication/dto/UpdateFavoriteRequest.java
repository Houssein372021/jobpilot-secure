package com.jobpilot.backend.jobapplication.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateFavoriteRequest(

        @NotNull(message = "La valeur favorite est obligatoire") Boolean favorite) {
}