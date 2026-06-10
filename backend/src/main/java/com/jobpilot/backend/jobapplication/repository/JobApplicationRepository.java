package com.jobpilot.backend.jobapplication.repository;

import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    Page<JobApplication> findByUserId(UUID userId, Pageable pageable);

    Page<JobApplication> findByUserIdAndStatus(
            UUID userId,
            ApplicationStatus status,
            Pageable pageable);

    Optional<JobApplication> findByIdAndUserId(UUID id, UUID userId);

    long countByUserId(UUID userId);

    long countByUserIdAndStatus(UUID userId, ApplicationStatus status);

    @Query("""
            SELECT j FROM JobApplication j
            WHERE j.user.id = :userId
            AND (
                LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.location) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.notes) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            """)
    Page<JobApplication> searchForUser(
            @Param("userId") UUID userId,
            @Param("search") String search,
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

    List<JobApplication> findByUserIdAndSource(
            UUID userId,
            String source);

    long countByUserIdAndFavorite(UUID userId, boolean favorite);

    @Query("""
            SELECT j FROM JobApplication j
            WHERE j.user.id = :userId
            AND (
                :status IS NULL OR j.status = :status
            )
            AND (
                :favorite IS NULL OR j.favorite = :favorite
            )
            AND (
                :search IS NULL
                OR :search = ''
                OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.location) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(j.notes) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            """)
    Page<JobApplication> findAllForUserWithFilters(
            @Param("userId") UUID userId,
            @Param("status") ApplicationStatus status,
            @Param("favorite") Boolean favorite,
            @Param("search") String search,
            Pageable pageable);

    @Query("""
            SELECT j FROM JobApplication j
            WHERE j.user.id = :userId
            AND j.followUpAt IS NOT NULL
            AND j.followUpAt >= :now
            ORDER BY j.followUpAt ASC
            """)
    Page<JobApplication> findUpcomingFollowUpsForUser(
            @Param("userId") UUID userId,
            @Param("now") LocalDateTime now,
            Pageable pageable);

    @Query("""
            SELECT j FROM JobApplication j
            WHERE j.user.id = :userId
            AND j.followUpAt IS NOT NULL
            AND j.followUpAt < :now
            ORDER BY j.followUpAt ASC
            """)
    Page<JobApplication> findOverdueFollowUpsForUser(
            @Param("userId") UUID userId,
            @Param("now") LocalDateTime now,
            Pageable pageable);

    @Query("""
            SELECT j FROM JobApplication j
            WHERE j.user.id = :userId
            AND j.followUpAt IS NOT NULL
            AND j.followUpAt >= :startOfDay
            AND j.followUpAt < :endOfDay
            ORDER BY j.followUpAt ASC
            """)
    Page<JobApplication> findTodayFollowUpsForUser(
            @Param("userId") UUID userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            Pageable pageable);

    @Query("""
            SELECT COUNT(j) FROM JobApplication j
            WHERE j.user.id = :userId
            AND j.followUpAt IS NOT NULL
            AND j.followUpAt >= :startOfDay
            AND j.followUpAt < :endOfDay
            """)
    long countTodayFollowUpsForUser(
            @Param("userId") UUID userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
            SELECT COUNT(j) FROM JobApplication j
            WHERE j.user.id = :userId
            AND j.followUpAt IS NOT NULL
            AND j.followUpAt < :now
            """)
    long countOverdueFollowUpsForUser(
            @Param("userId") UUID userId,
            @Param("now") LocalDateTime now);

    @Query("""
            SELECT COUNT(j) FROM JobApplication j
            WHERE j.user.id = :userId
            AND j.followUpAt IS NOT NULL
            AND j.followUpAt >= :now
            """)
    long countUpcomingFollowUpsForUser(
            @Param("userId") UUID userId,
            @Param("now") LocalDateTime now);

}