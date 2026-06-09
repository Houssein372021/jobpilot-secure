package com.jobpilot.backend.jobapplication.service;

import com.jobpilot.backend.jobapplication.dto.CreateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.dto.UpdateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public JobApplicationResponse create(User user, CreateJobApplicationRequest request) {
        JobApplication jobApplication = new JobApplication();

        jobApplication.setId(UUID.randomUUID());
        jobApplication.setUser(user);
        jobApplication.setCompanyName(request.companyName());
        jobApplication.setJobTitle(request.jobTitle());
        jobApplication.setLocation(request.location());
        jobApplication.setContractType(request.contractType());
        jobApplication.setStatus(ApplicationStatus.SAVED);
        jobApplication.setSource(request.source());
        jobApplication.setApplicationUrl(request.applicationUrl());
        jobApplication.setNotes(request.notes());
        jobApplication.setCreatedAt(LocalDateTime.now());

        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);

        return toResponse(savedApplication);
    }

    private JobApplicationResponse toResponse(JobApplication jobApplication) {
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
                jobApplication.getUpdatedAt()
        );
    }

    public List<JobApplicationResponse> findAllForUser(User user) {
        return jobApplicationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public JobApplicationResponse findByIdForUser(User user, UUID applicationId) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidature introuvable"));

        return toResponse(jobApplication);
    }

    public JobApplicationResponse update(
            User user,
            UUID applicationId,
            UpdateJobApplicationRequest request
    ) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidature introuvable"));

        if (request.companyName() != null) {
            jobApplication.setCompanyName(request.companyName());
        }

        if (request.jobTitle() != null) {
            jobApplication.setJobTitle(request.jobTitle());
        }

        if (request.location() != null) {
            jobApplication.setLocation(request.location());
        }

        if (request.contractType() != null) {
            jobApplication.setContractType(request.contractType());
        }

        if (request.status() != null) {
            jobApplication.setStatus(request.status());
        }

        if (request.source() != null) {
            jobApplication.setSource(request.source());
        }

        if (request.applicationUrl() != null) {
            jobApplication.setApplicationUrl(request.applicationUrl());
        }

        if (request.notes() != null) {
            jobApplication.setNotes(request.notes());
        }

        jobApplication.setUpdatedAt(LocalDateTime.now());

        JobApplication updatedApplication = jobApplicationRepository.save(jobApplication);

        return toResponse(updatedApplication);
    }

    public void delete(User user, UUID applicationId) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidature introuvable"));

        jobApplicationRepository.delete(jobApplication);
    }

    

}