package com.healthcareapp.healthcareapp.DTO;

import com.healthcareapp.healthcareapp.models.User;

public record UserResponse(Long id, String firstName, String lastName, String email, User.Role role) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getRole());
    }
}