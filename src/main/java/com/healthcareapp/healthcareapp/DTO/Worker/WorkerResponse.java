package com.healthcareapp.healthcareapp.DTO.Worker;

import com.healthcareapp.healthcareapp.models.Departments;
import com.healthcareapp.healthcareapp.models.User;
import com.healthcareapp.healthcareapp.models.Worker;

import java.time.LocalDate;

public record WorkerResponse(
        Long id,
        Long userId,
        String firstName,
        String lastName,
        String email,
        String role,

        String employeeNumber,
        Departments department,
        String jobTitle,
        LocalDate hireDate,
        String phone,
        boolean active
) {

    public static WorkerResponse from(Worker worker) {

        User user = worker.getUser();

        return new WorkerResponse(
                worker.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getName(),

                worker.getEmployeeNumber(),
                worker.getDepartment(),
                worker.getJobTitle(),
                worker.getHireDate(),
                worker.getPhone(),
                worker.isActive()
        );
    }
}
