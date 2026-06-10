package com.jobpilot.backend.dashboard.controller;

import com.jobpilot.backend.dashboard.dto.DashboardStatsResponse;
import com.jobpilot.backend.dashboard.service.DashboardService;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.user.entity.User;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardStatsResponse getStats(
            @AuthenticationPrincipal User user) {
        return dashboardService.getStats(user);
    }

    @GetMapping("/recent-applications")
    public List<JobApplicationResponse> getRecentApplications(
            @AuthenticationPrincipal User user) {
        return dashboardService.getRecentApplications(user);
    }

    @GetMapping("/upcoming-follow-ups")
    public List<JobApplicationResponse> getUpcomingFollowUps(
            @AuthenticationPrincipal User user) {
        return dashboardService.getUpcomingFollowUps(user);
    }
}