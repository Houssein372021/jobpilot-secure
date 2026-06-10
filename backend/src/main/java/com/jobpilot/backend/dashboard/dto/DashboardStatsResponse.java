package com.jobpilot.backend.dashboard.dto;

public record DashboardStatsResponse(
        long total,
        long saved,
        long applied,
        long interview,
        long offer,
        long rejected,
        long withdrawn,
        long favorites) {
}