package com.jobpilot.backend.user.service;

import com.jobpilot.backend.user.dto.UserProfileResponse;
import com.jobpilot.backend.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserProfileResponse getProfile(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
    }
}