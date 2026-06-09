package com.jobpilot.backend.jobapplication.repository;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    long countByUserIdAndStatus(UUID userId, ApplicationStatus status);

    @Query("""
            SELECT j FROM JobApplication j
            WHERE j.user.id = :userId
            AND (
                LOWER(j.companyName) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(j.location) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(j.source) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            ORDER BY j.createdAt DESC
            """)
    List<JobApplication> searchForUser(
            UUID userId,
            String query
    );

}