package com.jobpilot.backend.jobapplication.service;

import com.jobpilot.backend.common.dto.PagedResponse;
import com.jobpilot.backend.common.exception.ResourceNotFoundException;
import com.jobpilot.backend.jobapplication.dto.CreateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.dto.JobApplicationStatsResponse;
import com.jobpilot.backend.jobapplication.dto.UpdateApplicationStatusRequest;
import com.jobpilot.backend.jobapplication.dto.UpdateFavoriteRequest;
import com.jobpilot.backend.jobapplication.dto.UpdateFollowUpRequest;
import com.jobpilot.backend.jobapplication.dto.UpdateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        jobApplication.setFollowUpAt(request.followUpAt());
        jobApplication.setNotes(request.notes());

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
                jobApplication.getUpdatedAt(),
                jobApplication.getFollowUpAt(),
                jobApplication.isFavorite());
    }

    public PagedResponse<JobApplicationResponse> findAllForUser(
            User user,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository.findByUserId(user.getId(), pageable);

        return toPagedResponse(page);
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

        if (request.followUpAt() != null) {
            jobApplication.setFollowUpAt(request.followUpAt());
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

        if (request.favorite() != null) {
            jobApplication.setFavorite(request.favorite());
        }

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

        return toPagedResponse(page);
    }

    public JobApplicationStatsResponse getStats(User user) {
        long saved = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.SAVED);
        long applied = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.APPLIED);
        long interview = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.INTERVIEW);
        long offer = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.OFFER);
        long rejected = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.REJECTED);
        long withdrawn = jobApplicationRepository.countByUserIdAndStatus(user.getId(), ApplicationStatus.WITHDRAWN);
        long favorites = jobApplicationRepository.countByUserIdAndFavorite(user.getId(), true);

        long total = saved + applied + interview + offer + rejected + withdrawn;

        return new JobApplicationStatsResponse(
                total,
                saved,
                applied,
                interview,
                offer,
                rejected,
                withdrawn,
                favorites);
    }

    public PagedResponse<JobApplicationResponse> search(
            User user,
            String query,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository
                .searchForUser(user.getId(), query, pageable);

        return toPagedResponse(page);
    }

    public JobApplicationResponse updateStatus(
            User user,
            UUID applicationId,
            UpdateApplicationStatusRequest request) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        updateStatusAndAppliedDate(jobApplication, request.status());

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

    public PagedResponse<JobApplicationResponse> findAllForUserByFavorite(
            User user,
            boolean favorite,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository
                .findByUserIdAndFavorite(user.getId(), favorite, pageable);

        return toPagedResponse(page);
    }

    public PagedResponse<JobApplicationResponse> findAllForUserByStatusAndFavorite(
            User user,
            ApplicationStatus status,
            boolean favorite,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository
                .findByUserIdAndStatusAndFavorite(user.getId(), status, favorite, pageable);

        return toPagedResponse(page);
    }

    private PagedResponse<JobApplicationResponse> toPagedResponse(Page<JobApplication> page) {
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

    public JobApplicationResponse updateFavorite(
            User user,
            UUID applicationId,
            UpdateFavoriteRequest request) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        jobApplication.setFavorite(request.favorite());

        JobApplication updatedApplication = jobApplicationRepository.save(jobApplication);

        return toResponse(updatedApplication);
    }

    public List<JobApplicationResponse> createDemoApplications(User user) {
        boolean hasDemoApplications = jobApplicationRepository
                .findByUserId(user.getId(), Pageable.unpaged())
                .stream()
                .anyMatch(application -> "Demo".equals(application.getSource()));

        if (hasDemoApplications) {
            throw new IllegalArgumentException("Les candidatures de démonstration existent déjà");
        }
        List<JobApplication> applications = List.of(
                buildDemoApplication(user, "Capgemini", "Java Developer", "Nantes", "CDI", ApplicationStatus.APPLIED),
                buildDemoApplication(user, "Sopra Steria", "Backend Developer", "Nantes", "CDI",
                        ApplicationStatus.INTERVIEW),
                buildDemoApplication(user, "Orange", "Full Stack Developer", "Remote", "CDI", ApplicationStatus.SAVED));

        return jobApplicationRepository.saveAll(applications)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private JobApplication buildDemoApplication(
            User user,
            String companyName,
            String jobTitle,
            String location,
            String contractType,
            ApplicationStatus status) {
        JobApplication jobApplication = new JobApplication();

        jobApplication.setId(UUID.randomUUID());
        jobApplication.setUser(user);
        jobApplication.setCompanyName(companyName);
        jobApplication.setJobTitle(jobTitle);
        jobApplication.setLocation(location);
        jobApplication.setContractType(contractType);
        jobApplication.setStatus(status);
        jobApplication.setSource("Demo");
        jobApplication.setNotes("Candidature de démonstration");
        jobApplication.setFavorite(false);

        if (status == ApplicationStatus.APPLIED) {
            jobApplication.setAppliedAt(LocalDateTime.now());
        }

        return jobApplication;
    }

    public void deleteDemoApplications(User user) {
        List<JobApplication> demoApplications = jobApplicationRepository
                .findByUserIdAndSource(user.getId(), "Demo");

        jobApplicationRepository.deleteAll(demoApplications);
    }

    public PagedResponse<JobApplicationResponse> searchForUser(
            User user,
            String search,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository
                .searchForUser(user.getId(), search, pageable);

        return toPagedResponse(page);
    }

    public PagedResponse<JobApplicationResponse> findAllForUserWithFilters(
            User user,
            ApplicationStatus status,
            Boolean favorite,
            String search,
            Pageable pageable) {
        Page<JobApplication> page = jobApplicationRepository.findAllForUserWithFilters(
                user.getId(),
                status,
                favorite,
                search,
                pageable);

        return toPagedResponse(page);
    }

    public JobApplicationResponse updateFollowUp(
            User user,
            UUID id,
            UpdateFollowUpRequest request) {
        JobApplication jobApplication = jobApplicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable"));

        if (jobApplication.getStatus() == ApplicationStatus.REJECTED
                || jobApplication.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new IllegalArgumentException("Impossible de modifier la relance d'une candidature terminée");
        }

        jobApplication.setFollowUpAt(request.followUpAt());

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return toResponse(savedJobApplication);
    }
}