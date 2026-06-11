package com.jobpilot.backend.dashboard.service;

import com.jobpilot.backend.dashboard.dto.DashboardActionSummaryResponse;
import com.jobpilot.backend.dashboard.dto.DashboardStatsResponse;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

        private static final int DASHBOARD_LIMIT = 5;
        private static final String CREATED_AT_FIELD = "createdAt";

        private final JobApplicationRepository jobApplicationRepository;

        public DashboardService(JobApplicationRepository jobApplicationRepository) {
                this.jobApplicationRepository = jobApplicationRepository;
        }

        public DashboardStatsResponse getStats(User user) {
                long total = jobApplicationRepository.countByUserId(user.getId());
                long saved = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.SAVED);
                long applied = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.APPLIED);
                long interview = jobApplicationRepository.countByUserIdAndStatus(user.getId(),
                                ApplicationStatus.INTERVIEW);
                long offer = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.OFFER);
                long rejected = jobApplicationRepository.countByUserIdAndStatus(user.getId(),
                                ApplicationStatus.REJECTED);
                long withdrawn = jobApplicationRepository.countByUserIdAndStatus(user.getId(),
                                ApplicationStatus.WITHDRAWN);
                long favorites = jobApplicationRepository.countByUserIdAndFavorite(user.getId(), true);

                return new DashboardStatsResponse(
                                total,
                                saved,
                                applied,
                                interview,
                                offer,
                                rejected,
                                withdrawn,
                                favorites);
        }

        public List<JobApplicationResponse> getRecentApplications(User user) {
                PageRequest pageRequest = recentApplicationsPageRequest();

                return toJobApplicationResponses(jobApplicationRepository.findByUserId(user.getId(), pageRequest));
        }

        private JobApplicationResponse toJobApplicationResponse(JobApplication jobApplication) {
                return new JobApplicationResponse(
                                jobApplication.getId(),
                                jobApplication.getCompanyName(),
                                jobApplication.getJobTitle(),
                                jobApplication.getLocation(),
                                jobApplication.getContractType(),
                                jobApplication.getStatus().name(),
                                jobApplication.getSource(),
                                jobApplication.getApplicationUrl(),
                                jobApplication.getNotes(),
                                jobApplication.getAppliedAt(),
                                jobApplication.getCreatedAt(),
                                jobApplication.getUpdatedAt(),
                                jobApplication.getFollowUpAt(),
                                jobApplication.isFavorite());
        }

        public List<JobApplicationResponse> getUpcomingFollowUps(User user) {
                PageRequest pageRequest = defaultDashboardPageRequest();

                return toJobApplicationResponses(
                                jobApplicationRepository.findUpcomingFollowUpsForUser(
                                                user.getId(),
                                                LocalDateTime.now(),
                                                getExcludedFollowUpStatuses(),
                                                pageRequest));
        }

        public List<JobApplicationResponse> getOverdueFollowUps(User user) {
                PageRequest pageRequest = defaultDashboardPageRequest();

                return toJobApplicationResponses(
                                jobApplicationRepository.findOverdueFollowUpsForUser(
                                                user.getId(),
                                                LocalDateTime.now(),
                                                getExcludedFollowUpStatuses(),
                                                pageRequest));
        }

        public List<JobApplicationResponse> getTodayFollowUps(User user) {
                LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
                LocalDateTime endOfDay = startOfDay.plusDays(1);

                PageRequest pageRequest = defaultDashboardPageRequest();

                return toJobApplicationResponses(
                                jobApplicationRepository.findTodayFollowUpsForUser(
                                                user.getId(),
                                                startOfDay,
                                                endOfDay,
                                                getExcludedFollowUpStatuses(),
                                                pageRequest));
        }

        public DashboardActionSummaryResponse getActionSummary(User user) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
                LocalDateTime endOfDay = startOfDay.plusDays(1);

                long todayFollowUps = jobApplicationRepository.countTodayFollowUpsForUser(
                                user.getId(),
                                startOfDay,
                                endOfDay,
                                getExcludedFollowUpStatuses());

                long overdueFollowUps = jobApplicationRepository.countOverdueFollowUpsForUser(
                                user.getId(),
                                now,
                                getExcludedFollowUpStatuses());

                long upcomingFollowUps = jobApplicationRepository.countUpcomingFollowUpsForUser(
                                user.getId(),
                                now,
                                getExcludedFollowUpStatuses());

                long applicationsWithoutFollowUp = jobApplicationRepository.countApplicationsWithoutFollowUpForUser(
                                user.getId(),
                                getExcludedFollowUpStatuses());

                long savedApplications = jobApplicationRepository.countByUserIdAndStatus(
                                user.getId(),
                                ApplicationStatus.SAVED);

                long interviewApplications = jobApplicationRepository.countByUserIdAndStatus(
                                user.getId(),
                                ApplicationStatus.INTERVIEW);

                return new DashboardActionSummaryResponse(
                                todayFollowUps,
                                overdueFollowUps,
                                upcomingFollowUps,
                                applicationsWithoutFollowUp,
                                savedApplications,
                                interviewApplications);
        }

        public List<JobApplicationResponse> getApplicationsWithoutFollowUp(User user) {
                PageRequest pageRequest = defaultDashboardPageRequest();

                return toJobApplicationResponses(
                                jobApplicationRepository.findApplicationsWithoutFollowUpForUser(
                                                user.getId(),
                                                getExcludedFollowUpStatuses(),
                                                pageRequest));
        }

        private PageRequest defaultDashboardPageRequest() {
                return PageRequest.of(0, DASHBOARD_LIMIT);
        }

        private PageRequest recentApplicationsPageRequest() {
                return PageRequest.of(
                                0,
                                DASHBOARD_LIMIT,
                                Sort.by(Sort.Direction.DESC, CREATED_AT_FIELD));
        }

        private List<JobApplicationResponse> toJobApplicationResponses(Page<JobApplication> page) {
                return page.getContent()
                                .stream()
                                .map(this::toJobApplicationResponse)
                                .toList();
        }

        private List<ApplicationStatus> getExcludedFollowUpStatuses() {
                return List.of(
                                ApplicationStatus.REJECTED,
                                ApplicationStatus.WITHDRAWN);
        }
}