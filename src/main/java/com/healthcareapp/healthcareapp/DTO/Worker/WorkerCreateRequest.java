package com.healthcareapp.healthcareapp.DTO.Worker;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

public record WorkerCreateRequest(
        @NonNull @Valid Long user_id,
        @NonNull @Valid String employeeNumber,
        @NonNull @Valid String jobTitle,
        @NonNull @Valid LocalDate hireDate,
        @NonNull @Valid String phone

) {

}
