package com.jobpilot.backend.dashboard.service;

import com.jobpilot.backend.dashboard.dto.DashboardStatsResponse;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final JobApplicationRepository jobApplicationRepository;

    public DashboardService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public DashboardStatsResponse getStats(User user) {
        long total = jobApplicationRepository.countByUserId(user.getId());
        long saved = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.SAVED);
        long applied = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.APPLIED);
        long interview = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.INTERVIEW);
        long offer = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.OFFER);
        long rejected = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.REJECTED);
        long withdrawn = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.WITHDRAWN);
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
        PageRequest pageRequest = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return jobApplicationRepository.findByUserId(user.getId(), pageRequest)
                .getContent()
                .stream()
                .map(this::toJobApplicationResponse)
                .toList();
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
        PageRequest pageRequest = PageRequest.of(
                0,
                5);

        return jobApplicationRepository
                .findUpcomingFollowUpsForUser(user.getId(), LocalDateTime.now(), pageRequest)
                .getContent()
                .stream()
                .map(this::toJobApplicationResponse)
                .toList();
    }

    public List<JobApplicationResponse> getOverdueFollowUps(User user) {
        PageRequest pageRequest = PageRequest.of(
                0,
                5);

        return jobApplicationRepository
                .findOverdueFollowUpsForUser(user.getId(), LocalDateTime.now(), pageRequest)
                .getContent()
                .stream()
                .map(this::toJobApplicationResponse)
                .toList();
    }

    public List<JobApplicationResponse> getTodayFollowUps(User user) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        PageRequest pageRequest = PageRequest.of(
                0,
                5);

        return jobApplicationRepository
                .findTodayFollowUpsForUser(user.getId(), startOfDay, endOfDay, pageRequest)
                .getContent()
                .stream()
                .map(this::toJobApplicationResponse)
                .toList();
    }
}