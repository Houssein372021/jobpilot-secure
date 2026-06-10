package com.jobpilot.backend.dashboard.service;

import com.jobpilot.backend.dashboard.dto.DashboardStatsResponse;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;
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
}