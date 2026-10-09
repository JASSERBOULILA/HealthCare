package com.healthcareapp.healthcareapp.DTO.permissions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionRequest(

        @NotBlank(message = "Permission name is required")
        @Size(max = 100, message = "Permission name is too long")
        String name

) {}
