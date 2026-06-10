package com.jobpilot.backend.jobapplication.repository;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

        Page<JobApplication> findByUserId(UUID userId, Pageable pageable);

        Page<JobApplication> findByUserIdAndStatus(
                        UUID userId,
                        ApplicationStatus status,
                        Pageable pageable);

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
        Page<JobApplication> searchForUser(
                        UUID userId,
                        String query,
                        Pageable pageable);

        Page<JobApplication> findByUserIdAndFavorite(
                        UUID userId,
                        boolean favorite,
                        Pageable pageable);

        Page<JobApplication> findByUserIdAndStatusAndFavorite(
                        UUID userId,
                        ApplicationStatus status,
                        boolean favorite,
                        Pageable pageable);

}