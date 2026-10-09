package com.healthcareapp.healthcareapp.DTO.permissions;

import com.healthcareapp.healthcareapp.models.Permission;

public record PermissionResponse(
        Long id,
        String name
) {
    public static PermissionResponse from(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getName()
        );
    }
}
