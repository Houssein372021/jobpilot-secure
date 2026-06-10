package com.jobpilot.backend.jobapplication.controller;

import com.jobpilot.backend.common.dto.PagedResponse;
import com.jobpilot.backend.common.util.PaginationUtils;
import com.jobpilot.backend.jobapplication.dto.CreateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.dto.JobApplicationStatsResponse;
import com.jobpilot.backend.jobapplication.dto.UpdateJobApplicationRequest;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.service.JobApplicationService;
import com.jobpilot.backend.user.entity.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateJobApplicationRequest request
    ) {
        return jobApplicationService.create(user, request);
    }

    @GetMapping("/{id}")
    public JobApplicationResponse findById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        return jobApplicationService.findByIdForUser(user, id);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateJobApplicationRequest request
    ) {
        return jobApplicationService.update(user, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        jobApplicationService.delete(user, id);
    }

    @GetMapping
    public PagedResponse<JobApplicationResponse> findAll(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(
                PaginationUtils.normalizePage(page),
                PaginationUtils.normalizeSize(size),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    
        if (status != null) {
            return jobApplicationService.findAllForUserByStatus(user, status, pageRequest);
        }
    
        return jobApplicationService.findAllForUser(user, pageRequest);
    }

    @GetMapping("/stats")
    public JobApplicationStatsResponse stats(
            @AuthenticationPrincipal User user
    ) {
        return jobApplicationService.getStats(user);
    }

    @GetMapping("/search")
    public PagedResponse<JobApplicationResponse> search(
            @AuthenticationPrincipal User user,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(
                PaginationUtils.normalizePage(page),
                PaginationUtils.normalizeSize(size),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return jobApplicationService.search(user, query, pageRequest);
    }


}