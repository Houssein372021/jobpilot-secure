package com.jobpilot.backend.jobapplication.repository;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    List<JobApplication> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<JobApplication> findByUserIdAndStatusOrderByCreatedAtDesc(
            UUID userId,
            ApplicationStatus status
    );

    Optional<JobApplication> findByIdAndUserId(UUID id, UUID userId);
}