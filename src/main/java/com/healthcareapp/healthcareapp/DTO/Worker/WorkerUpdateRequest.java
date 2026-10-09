package com.healthcareapp.healthcareapp.DTO.Worker;

public record WorkerUpdateRequest(
        String department,
        String jobTitle,
        String phone,
        Boolean active
) {}
