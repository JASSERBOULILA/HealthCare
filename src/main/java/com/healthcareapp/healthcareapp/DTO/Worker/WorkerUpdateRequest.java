package com.healthcareapp.healthcareapp.DTO.Worker;

import java.time.LocalDate;

public record WorkerUpdateRequest(
        String employeeNumber,
        String department,
        String jobTitle,
        String phone,
        Boolean active,
        LocalDate hireDate
) {}
