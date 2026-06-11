package com.jobpilot.backend.dashboard.service;

import com.jobpilot.backend.dashboard.dto.DashboardActionSummaryResponse;
import com.jobpilot.backend.dashboard.dto.DashboardStatsResponse;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private User user;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        when(user.getId()).thenReturn(USER_ID);
    }

    @Test
    void shouldReturnDashboardStats() {
        when(jobApplicationRepository.countByUserId(USER_ID)).thenReturn(10L);
        when(jobApplicationRepository.countByUserIdAndStatus(USER_ID, ApplicationStatus.SAVED)).thenReturn(2L);
        when(jobApplicationRepository.countByUserIdAndStatus(USER_ID, ApplicationStatus.APPLIED)).thenReturn(3L);
        when(jobApplicationRepository.countByUserIdAndStatus(USER_ID, ApplicationStatus.INTERVIEW)).thenReturn(1L);
        when(jobApplicationRepository.countByUserIdAndStatus(USER_ID, ApplicationStatus.OFFER)).thenReturn(1L);
        when(jobApplicationRepository.countByUserIdAndStatus(USER_ID, ApplicationStatus.REJECTED)).thenReturn(2L);
        when(jobApplicationRepository.countByUserIdAndStatus(USER_ID, ApplicationStatus.WITHDRAWN)).thenReturn(1L);
        when(jobApplicationRepository.countByUserIdAndFavorite(USER_ID, true)).thenReturn(4L);

        DashboardStatsResponse response = dashboardService.getStats(user);

        assertThat(response.total()).isEqualTo(10L);
        assertThat(response.saved()).isEqualTo(2L);
        assertThat(response.applied()).isEqualTo(3L);
        assertThat(response.interview()).isEqualTo(1L);
        assertThat(response.offer()).isEqualTo(1L);
        assertThat(response.rejected()).isEqualTo(2L);
        assertThat(response.withdrawn()).isEqualTo(1L);
        assertThat(response.favorites()).isEqualTo(4L);
    }

    @Test
    void shouldReturnActionSummary() {
        when(jobApplicationRepository.countTodayFollowUpsForUser(
                eq(USER_ID),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                anyList())).thenReturn(1L);

        when(jobApplicationRepository.countOverdueFollowUpsForUser(
                eq(USER_ID),
                any(LocalDateTime.class),
                anyList())).thenReturn(2L);

        when(jobApplicationRepository.countUpcomingFollowUpsForUser(
                eq(USER_ID),
                any(LocalDateTime.class),
                anyList())).thenReturn(3L);

        when(jobApplicationRepository.countApplicationsWithoutFollowUpForUser(
                eq(USER_ID),
                anyList())).thenReturn(4L);

        when(jobApplicationRepository.countByUserIdAndStatus(
                USER_ID,
                ApplicationStatus.SAVED)).thenReturn(5L);

        when(jobApplicationRepository.countByUserIdAndStatus(
                USER_ID,
                ApplicationStatus.INTERVIEW)).thenReturn(6L);

        DashboardActionSummaryResponse response = dashboardService.getActionSummary(user);

        assertThat(response.todayFollowUps()).isEqualTo(1L);
        assertThat(response.overdueFollowUps()).isEqualTo(2L);
        assertThat(response.upcomingFollowUps()).isEqualTo(3L);
        assertThat(response.applicationsWithoutFollowUp()).isEqualTo(4L);
        assertThat(response.savedApplications()).isEqualTo(5L);
        assertThat(response.interviewApplications()).isEqualTo(6L);
    }
}