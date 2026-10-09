package com.healthcareapp.healthcareapp.DTO.Worker;

import java.time.LocalDate;

public record WorkerCreateRequest(
        Long user_id,
        String employeeNumber,
        String department,
        String jobTitle,
        LocalDate hireDate,
        String phone

) {

}
