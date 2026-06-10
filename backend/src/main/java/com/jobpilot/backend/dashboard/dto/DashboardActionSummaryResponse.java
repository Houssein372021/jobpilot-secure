package com.jobpilot.backend.dashboard.dto;

public record DashboardActionSummaryResponse(
        long todayFollowUps,
        long overdueFollowUps,
        long upcomingFollowUps,
        long savedApplications,
        long interviewApplications) {
}