package com.jobpilot.backend.jobapplication.service;

import com.jobpilot.backend.jobapplication.dto.JobApplicationResponse;
import com.jobpilot.backend.jobapplication.dto.UpdateFollowUpRequest;
import com.jobpilot.backend.jobapplication.entity.ApplicationStatus;
import com.jobpilot.backend.jobapplication.entity.JobApplication;
import com.jobpilot.backend.jobapplication.repository.JobApplicationRepository;
import com.jobpilot.backend.user.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID APPLICATION_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 6, 11, 10, 0);

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private User user;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @BeforeEach
    void setUp() {
        when(user.getId()).thenReturn(USER_ID);
    }

    @Test
    void shouldUpdateFollowUpDate() {
        JobApplication jobApplication = createJobApplication(ApplicationStatus.SAVED);
        LocalDateTime followUpAt = LocalDateTime.of(2026, 6, 25, 10, 0);

        when(jobApplicationRepository.findByIdAndUserId(APPLICATION_ID, USER_ID))
                .thenReturn(Optional.of(jobApplication));

        when(jobApplicationRepository.save(jobApplication))
                .thenReturn(jobApplication);

        JobApplicationResponse response = jobApplicationService.updateFollowUp(
                user,
                APPLICATION_ID,
                new UpdateFollowUpRequest(followUpAt));

        assertThat(response.followUpAt()).isEqualTo(followUpAt);

        verify(jobApplicationRepository).save(jobApplication);
    }

    @Test
    void shouldRemoveFollowUpDate() {
        JobApplication jobApplication = createJobApplication(ApplicationStatus.SAVED);

        ReflectionTestUtils.setField(
                jobApplication,
                "followUpAt",
                LocalDateTime.of(2026, 6, 25, 10, 0));

        when(jobApplicationRepository.findByIdAndUserId(APPLICATION_ID, USER_ID))
                .thenReturn(Optional.of(jobApplication));

        when(jobApplicationRepository.save(jobApplication))
                .thenReturn(jobApplication);

        JobApplicationResponse response = jobApplicationService.updateFollowUp(
                user,
                APPLICATION_ID,
                new UpdateFollowUpRequest(null));

        assertThat(response.followUpAt()).isNull();

        verify(jobApplicationRepository).save(jobApplication);
    }

    @Test
    void shouldRejectFollowUpUpdateForRejectedApplication() {
        JobApplication jobApplication = createJobApplication(ApplicationStatus.REJECTED);

        when(jobApplicationRepository.findByIdAndUserId(APPLICATION_ID, USER_ID))
                .thenReturn(Optional.of(jobApplication));

        assertThatThrownBy(() -> jobApplicationService.updateFollowUp(
                user,
                APPLICATION_ID,
                new UpdateFollowUpRequest(LocalDateTime.of(2026, 6, 25, 10, 0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("candidature terminée");

        verify(jobApplicationRepository, never()).save(jobApplication);
    }

    @Test
    void shouldRejectFollowUpUpdateForWithdrawnApplication() {
        JobApplication jobApplication = createJobApplication(ApplicationStatus.WITHDRAWN);

        when(jobApplicationRepository.findByIdAndUserId(APPLICATION_ID, USER_ID))
                .thenReturn(Optional.of(jobApplication));

        assertThatThrownBy(() -> jobApplicationService.updateFollowUp(
                user,
                APPLICATION_ID,
                new UpdateFollowUpRequest(LocalDateTime.of(2026, 6, 25, 10, 0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("candidature terminée");

        verify(jobApplicationRepository, never()).save(jobApplication);
    }

    @Test
    void shouldRejectFollowUpDateBeforeCreationDate() {
        JobApplication jobApplication = createJobApplication(ApplicationStatus.SAVED);

        when(jobApplicationRepository.findByIdAndUserId(APPLICATION_ID, USER_ID))
                .thenReturn(Optional.of(jobApplication));

        assertThatThrownBy(() -> jobApplicationService.updateFollowUp(
                user,
                APPLICATION_ID,
                new UpdateFollowUpRequest(CREATED_AT.minusDays(1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("avant la date de création");

        verify(jobApplicationRepository, never()).save(jobApplication);
    }

    private JobApplication createJobApplication(ApplicationStatus status) {
        JobApplication jobApplication = new JobApplication();

        ReflectionTestUtils.setField(jobApplication, "id", APPLICATION_ID);
        ReflectionTestUtils.setField(jobApplication, "user", user);
        ReflectionTestUtils.setField(jobApplication, "companyName", "Test Company");
        ReflectionTestUtils.setField(jobApplication, "jobTitle", "Java Developer");
        ReflectionTestUtils.setField(jobApplication, "location", "Paris");
        ReflectionTestUtils.setField(jobApplication, "contractType", "CDI");
        ReflectionTestUtils.setField(jobApplication, "status", status);
        ReflectionTestUtils.setField(jobApplication, "source", "LinkedIn");
        ReflectionTestUtils.setField(jobApplication, "applicationUrl", "https://example.com");
        ReflectionTestUtils.setField(jobApplication, "notes", "Test notes");
        ReflectionTestUtils.setField(jobApplication, "createdAt", CREATED_AT);
        ReflectionTestUtils.setField(jobApplication, "favorite", false);

        return jobApplication;
    }
}