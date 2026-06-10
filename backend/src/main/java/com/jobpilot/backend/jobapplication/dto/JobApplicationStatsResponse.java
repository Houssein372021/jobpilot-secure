package com.jobpilot.backend.jobapplication.dto;

public record JobApplicationStatsResponse(
        long total,
        long saved,
        long applied,
        long interview,
        long offer,
        long rejected,
        long withdrawn,
        long favorite) {
}