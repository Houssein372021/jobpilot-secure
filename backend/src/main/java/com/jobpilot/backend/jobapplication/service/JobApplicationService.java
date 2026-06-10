package com.jobpilot.backend.jobapplication.service;

import com.jobpilot.backend.common.dto.PagedResponse;
import com.jobpilot.backend.common.exception.ResourceNotFoundException;
import com.jobpilot.backend.jobapplication.dto.CreateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.dto.JobApplicationStatsResponse;
import com.jobpilot.backend.jobapplication.dto.UpdateApplicationStatusRequest;
import com.jobpilot.backend.jobapplication.dto.UpdateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                jobApplication.getUpdatedAt());
    }

    public PagedResponse<JobApplicationResponse> findAllForUser(
            User user,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository.findByUserId(user.getId(), pageable);

        return new PagedResponse<>(
                page.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    public JobApplicationResponse findByIdForUser(User user, UUID applicationId) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        return toResponse(jobApplication);
    }

    public JobApplicationResponse update(
            User user,
            UUID applicationId,
            UpdateJobApplicationRequest request) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

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
            updateStatusAndAppliedDate(jobApplication, request.status());
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
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        jobApplicationRepository.delete(jobApplication);
    }

    public PagedResponse<JobApplicationResponse> findAllForUserByStatus(
            User user,
            ApplicationStatus status,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository
                .findByUserIdAndStatus(user.getId(), status, pageable);

        return new PagedResponse<>(
                page.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    public JobApplicationStatsResponse getStats(User user) {
        long saved = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.SAVED);
        long applied = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.APPLIED);
        long interview = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.INTERVIEW);
        long offer = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.OFFER);
        long rejected = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.REJECTED);
        long withdrawn = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.WITHDRAWN);

        long total = saved + applied + interview + offer + rejected + withdrawn;

        return new JobApplicationStatsResponse(
                total,
                saved,
                applied,
                interview,
                offer,
                rejected,
                withdrawn);
    }

    public PagedResponse<JobApplicationResponse> search(
            User user,
            String query,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository
                .searchForUser(user.getId(), query, pageable);

        return new PagedResponse<>(
                page.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    public JobApplicationResponse updateStatus(
            User user,
            UUID applicationId,
            UpdateApplicationStatusRequest request) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        updateStatusAndAppliedDate(jobApplication, request.status());

        jobApplication.setUpdatedAt(LocalDateTime.now());

        JobApplication updatedApplication = jobApplicationRepository.save(jobApplication);

        return toResponse(updatedApplication);
    }

    private void updateStatusAndAppliedDate(
            JobApplication jobApplication,
            ApplicationStatus newStatus) {
        ApplicationStatus currentStatus = jobApplication.getStatus();

        if (currentStatus == ApplicationStatus.REJECTED
                || currentStatus == ApplicationStatus.WITHDRAWN) {
            throw new IllegalArgumentException("Impossible de modifier le statut d'une candidature terminée");
        }

        jobApplication.setStatus(newStatus);

        if (newStatus == ApplicationStatus.APPLIED && jobApplication.getAppliedAt() == null) {
            jobApplication.setAppliedAt(LocalDateTime.now());
        }
    }

}